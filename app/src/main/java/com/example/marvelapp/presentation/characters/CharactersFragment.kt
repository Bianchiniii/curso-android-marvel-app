package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.marvelapp.R
import com.example.marvelapp.SortFragment.Companion.SORTING_APPLIED_BASK_STACK_KEY
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.framework.imageloader.GlideImageLoader
import com.example.marvelapp.presentation.characters.adapter.CharactersAdapter
import com.example.marvelapp.presentation.characters.adapter.CharactersLoadingMoreStateAdapter
import com.example.marvelapp.presentation.characters.adapter.CharactersRefreshStateAdapter
import com.example.marvelapp.presentation.details.DetailViewArg
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment(), MenuProvider, SearchView.OnQueryTextListener,
    MenuItem.OnActionExpandListener {

    private lateinit var binding: FragmentCharactersBinding

    private val viewModel: CharacterViewModel by viewModels()

    @Inject
    lateinit var imageLoader: GlideImageLoader

    private lateinit var searchView: SearchView

    private val charactersAdapter by lazy {
        CharactersAdapter(imageLoader) { character, view ->
            val extras = FragmentNavigatorExtras(
                view to character.name
            )
            val directions = CharactersFragmentDirections
                .actionCharactersFragmentToDetailFragment(
                    character.name,
                    DetailViewArg(
                        character.id,
                        character.name,
                        character.imageUrl
                    )
                )

            findNavController().navigate(directions, extras)
        }
    }

    private val headerAdapter: CharactersRefreshStateAdapter by lazy {
        CharactersRefreshStateAdapter(charactersAdapter::retry)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersBinding.inflate(layoutInflater, container, false)

        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.characters_menu_items, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        searchView = searchItem.actionView as SearchView

        searchItem.setOnActionExpandListener(this)

        if (viewModel.currentSearchQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(viewModel.currentSearchQuery, false)
        }

        searchView.apply {
            isSubmitButtonEnabled = true
            setOnQueryTextListener(this@CharactersFragment)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_sort -> {
                findNavController().navigate(R.id.action_charactersFragment_to_sortFragment)

                true
            }

            else -> false
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return query?.let {
            viewModel.currentSearchQuery = it
            viewModel.searchCharacters()
            true
        } ?: false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCharactersAdapter()
        observeInitialLoadState()
        observeSortingData()

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is CharacterViewModel.UiState.SearchResult -> {
                    charactersAdapter.submitData(
                        viewLifecycleOwner.lifecycle,
                        state.data
                    )
                }
            }
        }
    }

    private fun observeSortingData() {
        // getBackStackEntry pq SortFragment é um BottomSheetDialogFragment
        val navBackEntry = findNavController().getBackStackEntry(R.id.charactersFragment)
        val observer = LifecycleEventObserver { _, event ->
            val isSortingApply =
                navBackEntry.savedStateHandle.contains(SORTING_APPLIED_BASK_STACK_KEY)

            if (event == Lifecycle.Event.ON_RESUME && isSortingApply) {
                navBackEntry.savedStateHandle.remove<Boolean>(SORTING_APPLIED_BASK_STACK_KEY)

                viewModel.applySort()
            }
        }

        navBackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
       return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
        viewModel.closeSearch()
        viewModel.searchCharacters()

        return true
    }

    private fun initCharactersAdapter() {
        postponeEnterTransition()

        with(binding.recyclerCharacters) {
            setHasFixedSize(true)
            adapter = charactersAdapter.withLoadStateHeaderAndFooter(
                footer = CharactersLoadingMoreStateAdapter { charactersAdapter.retry() },
                header = headerAdapter
            )
            viewTreeObserver.addOnPreDrawListener {

                startPostponedEnterTransition()

                true
            }
        }
    }

    private fun observeInitialLoadState() {
        lifecycleScope.launch {
            charactersAdapter.loadStateFlow.collectLatest { loadingState ->
                headerAdapter.loadState = loadingState.mediator
                    ?.refresh
                    ?.takeIf {
                        it is LoadState.Error && charactersAdapter.itemCount > 0
                    } ?: loadingState.prepend

                binding.flipperCharacters.displayedChild = when {
                    loadingState.mediator?.refresh is LoadState.Loading -> {
                        setShimmerVisibility(true)
                        FLIPPER_CHILD_LOADING
                    }

                    loadingState.mediator?.refresh is LoadState.Error
                            && charactersAdapter.itemCount == EMPTY -> {
                        setShimmerVisibility(false)
                        binding.includeViewCharactersErrorState.buttonRetry.setOnClickListener {
                            charactersAdapter.retry()
                        }
                        FLIPPER_CHILD_ERROR
                    }

                    loadingState.source.refresh is LoadState.NotLoading
                            || loadingState.mediator?.refresh is LoadState.NotLoading -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTERS
                    }

                    else -> {
                        setShimmerVisibility(false)
                        FLIPPER_CHILD_CHARACTERS
                    }
                }
            }
        }
    }

    private fun setShimmerVisibility(isVisible: Boolean) {
        binding.includeViewCharactersLoadingState.shimmerCharacters.run {
            when (isVisible) {
                true -> {
                    this.visibility = View.VISIBLE

                    startShimmer()
                }

                false -> {
                    this.visibility = View.GONE

                    hideShimmer()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        searchView.setOnQueryTextListener(null)
    }

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_CHARACTERS = 1
        private const val FLIPPER_CHILD_ERROR = 2

        private const val EMPTY = 0
    }
}