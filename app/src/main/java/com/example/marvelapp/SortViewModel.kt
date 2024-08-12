package com.example.marvelapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.bianchini.vinicius.matheus.core.usecase.GetCharactersSortingUseCase
import com.bianchini.vinicius.matheus.core.usecase.SaveCharactersSortingUseCase
import com.bianchini.vinicius.matheus.core.usecase.base.CoroutinesDispatchers
import com.example.marvelapp.utils.watchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SortViewModel @Inject constructor(
    private val getCharactersSortingUseCase: GetCharactersSortingUseCase,
    private val saveCharactersSortingUseCase: SaveCharactersSortingUseCase,
    private val coroutinesDispatchers: CoroutinesDispatchers
) : ViewModel() {

    private val _action = MutableLiveData<Action>(Action.GetStorageAction)
    val state: LiveData<UiState> = _action.switchMap { action ->
        liveData(coroutinesDispatchers.main()) {
            when (action) {

                is Action.ApplySorting -> {
                    val orderBy = action.orderBy
                    val order = action.order

                    saveCharactersSortingUseCase.invoke(
                        SaveCharactersSortingUseCase.SaveCharactersSortingUseCaseParams(
                            orderBy to order
                        )
                    ).watchStatus(
                        loading = { emit(UiState.ApplyState.Loading) },
                        success = { emit(UiState.ApplyState.Success) },
                        error = { emit(UiState.ApplyState.Error) },
                    )
                }

                Action.GetStorageAction -> {
                    getCharactersSortingUseCase.invoke().collect { sortingPair ->
                        emit(
                            UiState.SortingResult(sortingPair)
                        )
                    }
                }
            }
        }
    }


    fun applySorting(
        orderBy: String,
        order: String
    ) {
        _action.value = Action.ApplySorting(orderBy, order)
    }

    sealed class UiState {

        data class SortingResult(
            val storageSort: Pair<String, String>
        ) : UiState()

        sealed class ApplyState : UiState() {

            object Loading : ApplyState()

            object Success : ApplyState()

            object Error : ApplyState()
        }
    }

    sealed class Action {

        object GetStorageAction : Action()

        data class ApplySorting(
            val orderBy: String,
            val order: String
        ) : Action()
    }
}