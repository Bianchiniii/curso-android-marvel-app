package com.example.marvelapp.presentation.details

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.bianchini.vinicius.matheus.core.usecase.AddFavoriteUseCase
import com.example.marvelapp.R
import com.example.marvelapp.utils.watchStatus
import kotlin.coroutines.CoroutineContext

class UiActionFavoriteStateFlow(
    private val coroutineContext: CoroutineContext,
    private val addFavoriteUseCase: AddFavoriteUseCase,
) {

    private val action = MutableLiveData<Action>()

    val state: LiveData<UiState> = action.switchMap {
        liveData(coroutineContext) {
            when (it) {
                is Action.Update -> {
                    it.detailViewArg.run {
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
                                emit(UiState.Icon(R.drawable.ic_favorite_checked))
                            },
                            error = {
                                emit(UiState.Error(R.string.error_loading_favorite))
                            }
                        )
                    }
                }

                Action.Default -> emit(UiState.Icon(R.drawable.ic_favorite_unchecked))
            }
        }
    }


    fun setDefault() {
        action.value = Action.Default
    }

    fun update(detailViewArg: DetailViewArg) {
        action.value = Action.Update(detailViewArg)
    }

    sealed class UiState {
        object Loading : UiState()
        class Icon(@DrawableRes val icon: Int) : UiState()
        data class Error(@StringRes val message: Int) : UiState()
    }

    sealed class Action {
        object Default : Action()

        data class Update(val detailViewArg: DetailViewArg) : Action()
    }
}