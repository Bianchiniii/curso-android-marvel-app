package com.example.marvelapp.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.marvelapp.databinding.FragmentFavoritesBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.commun.getGenericAdapterOf
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var imageLoader: ImageLoader

    private val favoriteAdapter by lazy {
        getGenericAdapterOf {
            FavoritesViewHolder.create(it, imageLoader)
        }
    }

    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFavoritesBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObservers()

        viewModel.getAll()
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.flipperFavorites.displayedChild = when (state) {
                FavoriteViewModel.UiState.ShowEmpty -> {
                    favoriteAdapter.submitList(emptyList())

                    FLIPPER_CHILD_EMPTY
                }

                is FavoriteViewModel.UiState.ShowFavorites -> {
                    favoriteAdapter.submitList(state.favorites)

                    FLIPPER_CHILD_CHARACTERS
                }
            }
        }
    }

    private fun setupView() {
        binding.recyclerFavorites.run {
            this.adapter = favoriteAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_CHARACTERS = 0
        private const val FLIPPER_CHILD_EMPTY = 1
    }

}