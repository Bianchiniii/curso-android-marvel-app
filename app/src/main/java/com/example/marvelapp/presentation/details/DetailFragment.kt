package com.example.marvelapp.presentation.details

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.framework.imageloader.GlideImageLoader
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    private val args by navArgs<DetailFragmentArgs>()

    @Inject
    lateinit var imageLoader: GlideImageLoader


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(
        layoutInflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val detailViewArgs = args.detailViewArg
        binding.imageCharacter.run {
            transitionName = detailViewArgs.name
            imageLoader.load(this, detailViewArgs.imageUrl)
        }

        setSharedElementTransitionOnEnter()

        observeUiState(detailViewArgs)
        observeFavoriteUiState()

        viewModel.categories.load(detailViewArgs.id)

        binding.imageFavoriteIcon.setOnClickListener {
            viewModel.favorite.update(detailViewArgs)
        }
    }

    private fun observeFavoriteUiState() {
        viewModel.favorite.state.observe(viewLifecycleOwner) {
            binding.flipperDetail.displayedChild = when (it) {
                UiActionFavoriteStateFlow.UiState.Loading -> FLIP_FAVORITE_LOADING_STATE

                is UiActionFavoriteStateFlow.UiState.Icon -> {
                    binding.imageFavoriteIcon.setImageResource(it.icon)

                    FLIP_FAVORITE_SUCCESS_STATE
                }

                is UiActionFavoriteStateFlow.UiState.Error -> {
                    Snackbar.make(
                        binding.root,
                        it.message,
                        Snackbar.LENGTH_SHORT
                    ).show()

                    FLIP_FAVORITE_ERROR_STATE
                }
            }
        }
    }

    private fun observeUiState(detailViewArgs: DetailViewArg) {
        viewModel.categories.state.observe(viewLifecycleOwner) {
            binding.flipperDetail.displayedChild = when (it) {
                UiActionStateFlow.UiState.Empty -> FLIPPER_CHILD_POSITION_EMPTY

                UiActionStateFlow.UiState.Error -> {
                    binding.includeErrorView.buttonRetry.setOnClickListener {
                        viewModel.categories.load(detailViewArgs.id)
                    }

                    FLIPPER_CHILD_POSITION_ERROR
                }

                UiActionStateFlow.UiState.Loading -> FLIPPER_CHILD_POSITION_LOADING

                is UiActionStateFlow.UiState.Success -> binding.recyclerParentDetail.run {
                    setHasFixedSize(true)
                    adapter = DetailParentAdapter(it.detailParentVE, imageLoader)

                    FLIPPER_CHILD_POSITION_SUCCESS
                }
            }
        }
    }

    // Define a animação da transição como "move"
    private fun setSharedElementTransitionOnEnter() {
        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply {
                sharedElementEnterTransition = this
            }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    companion object {
        private const val FLIPPER_CHILD_POSITION_LOADING = 0
        private const val FLIPPER_CHILD_POSITION_SUCCESS = 1
        private const val FLIPPER_CHILD_POSITION_ERROR = 2
        private const val FLIPPER_CHILD_POSITION_EMPTY = 3

        private const val FLIP_FAVORITE_LOADING_STATE = 4
        private const val FLIP_FAVORITE_SUCCESS_STATE = 5
        private const val FLIP_FAVORITE_ERROR_STATE = 6
    }
}