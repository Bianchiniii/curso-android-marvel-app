package com.example.marvelapp.presentation.details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.bianchini.vinicius.matheus.core.usecase.AddFavoriteUseCase
import com.bianchini.vinicius.matheus.core.usecase.IsFavoriteUseCase
import com.bianchini.vinicius.matheus.core.usecase.RemoveFavoriteUseCase
import com.example.marvelapp.R
import com.example.marvelapp.utils.watchStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.coroutines.CoroutineContext

class UiActionFavoriteStateFlow(
    private val coroutineContext: CoroutineContext,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
) {

    private var favoriteState = MutableStateFlow(false)

    private val action = MutableLiveData<Action>()
    val state: LiveData<UiState> = action.switchMap { action ->
        liveData(coroutineContext) {
            when (action) {
                is Action.AddFavorite -> {
                    action.detailViewArg.run {
                        addFavoriteUseCase(
                            AddFavoriteUseCase.AddFavoriteUseCaseParams(
                                id,
                                name,
                                imageUrl
                            )
                        ).watchStatus(
                            loading = {
                                emit(UiState.Loading)
                            },
                            success = {
                                favoriteState.update { true }

                                emit(UiState.Icon(R.drawable.ic_favorite_checked))
                            },
                            error = {
                                emit(UiState.Error(R.string.error_add_favorite))
                            }
                        )
                    }
                }

                is Action.IsFavorite -> {
                    isFavoriteUseCase(
                        IsFavoriteUseCase.Params(action.characterId)
                    ).watchStatus(
                        loading = {},
                        success = {
                            favoriteState.update { it }

                            if (it) {
                                emit(UiState.Icon(R.drawable.ic_favorite_checked))
                            } else emit(UiState.Icon(R.drawable.ic_favorite_unchecked))
                        },
                        error = {}
                    )

                }

                is Action.RemoveFavorite -> {
                    action.detailViewArg.run {
                        removeFavoriteUseCase(
                            RemoveFavoriteUseCase.Params(
                                id,
                                name,
                                imageUrl
                            )
                        ).watchStatus(
                            loading = {
                                favoriteState.update { false }

                                emit(UiState.Loading)
                            },
                            success = {
                                emit(UiState.Icon(R.drawable.ic_favorite_unchecked))
                            },
                            error = {
                                emit(UiState.Error(R.string.error_remove_favorite))
                            }
                        )
                    }

                }
            }
        }
    }


    fun isFavorite(characterId: Int) {
        action.value = Action.IsFavorite(characterId)
    }

    fun update(detailViewArg: DetailViewArg) {
        action.value = if (favoriteState.value) {
            Action.RemoveFavorite(detailViewArg)
        } else Action.AddFavorite(detailViewArg)
    }

    sealed class UiState {
        object Loading : UiState()
        class Icon(@DrawableRes val icon: Int) : UiState()
        data class Error(@StringRes val message: Int) : UiState()
    }

    sealed class Action {
        data class IsFavorite(
            val characterId: Int
        ) : Action()

        data class AddFavorite(val detailViewArg: DetailViewArg) : Action()
        data class RemoveFavorite(val detailViewArg: DetailViewArg) : Action()
    }
}