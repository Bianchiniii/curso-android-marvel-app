package com.example.marvelapp.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.bianchini.vinicius.matheus.core.usecase.GetCharacterCategoriesUseCase
import com.example.marvelapp.R
import com.example.marvelapp.utils.watchStatus
import kotlin.coroutines.CoroutineContext

class UiActionStateFlow(
    private val coroutineContext: CoroutineContext,
    private val getCharacterCategories: GetCharacterCategoriesUseCase
) {

    private val action = MutableLiveData<Action>()
    val state: LiveData<UiState> = action.switchMap {
        liveData(coroutineContext) {
            when (it) {
                is Action.Load -> {
                    getCharacterCategories.invoke(
                        GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(it.characterId)
                    ).watchStatus(
                        loading = {
                            emit(UiState.Loading)
                        },
                        success = { data ->
                            val detailParentList = mutableListOf<DetailParentVE>()

                            val comics = data.first
                            if (comics.isNotEmpty()) {
                                comics.map {
                                    DetailChildVE(it.id, it.thumbnail)
                                }.also {
                                    detailParentList.add(
                                        DetailParentVE(R.string.details_comics_category, it)
                                    )
                                }
                            }

                            val events = data.second
                            if (events.isNotEmpty()) {
                                events.map {
                                    DetailChildVE(it.id, it.thumbnail)
                                }.also {
                                    detailParentList.add(
                                        DetailParentVE(R.string.details_events_category, it)
                                    )
                                }
                            }

                            if (detailParentList.isNotEmpty()) {
                                emit(UiState.Success(detailParentList))
                            } else emit(UiState.Empty)
                        },
                        error = {
                            emit(UiState.Error)
                        }
                    )
                }
            }
        }
    }

    fun load(characterId: Int) {
        action.value = Action.Load(characterId)
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val detailParentVE: List<DetailParentVE>) : UiState()
        object Error : UiState()
        object Empty : UiState()
    }

    sealed class Action {
        data class Load(val characterId: Int) : Action()
    }
}