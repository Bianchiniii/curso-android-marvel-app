package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
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
class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding

    private val viewModel: CharacterViewModel by viewModels()

    @Inject
    lateinit var imageLoader: GlideImageLoader

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
    ) = FragmentCharactersBinding.inflate(layoutInflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCharactersAdapter()
        observeInitialLoadState()

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

    companion object {
        private const val FLIPPER_CHILD_LOADING = 0
        private const val FLIPPER_CHILD_CHARACTERS = 1
        private const val FLIPPER_CHILD_ERROR = 2

        private const val EMPTY = 0
    }
}