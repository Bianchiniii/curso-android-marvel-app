package com.example.marvelapp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.bianchini.vinicius.matheus.core.domain.model.Event
import com.bianchini.vinicius.matheus.core.usecase.GetCharacterCategoriesUseCase
import com.bianchini.vinicius.matheus.core.usecase.base.ResultStatus
import com.example.marvelapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiStateFlow

    fun getCharactersCategories(characterId: Int) = viewModelScope.launch {
        getCharacterCategoriesUseCase(
            GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(characterId)
        ).watchStatus()
    }

    private fun Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>.watchStatus() =
        viewModelScope.launch {
            collect { status ->
                _uiStateFlow.update {
                    when (status) {
                        is ResultStatus.Error -> UiState.Error
                        ResultStatus.Loading -> UiState.Loading
                        is ResultStatus.Success -> {
                            val detailsParentList = buildList {
                                val comics = status.data.first
                                if (comics.isNotEmpty()) {
                                    comics.map {
                                        DetailChildVE(
                                            it.id,
                                            it.thumbnail
                                        )
                                    }.also {
                                        add(
                                            DetailParentVE(R.string.details_comics_category, it)
                                        )
                                    }
                                }

                                val events = status.data.second
                                if (events.isNotEmpty()) {
                                    events.map {
                                        DetailChildVE(
                                            it.id,
                                            it.thumbnail
                                        )
                                    }.also {
                                        add(
                                            DetailParentVE(R.string.details_events_category, it)
                                        )
                                    }
                                }
                            }

                            if (detailsParentList.isEmpty()) {
                                UiState.Empty
                            } else UiState.Success(detailsParentList)
                        }
                    }
                }
            }
        }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val detailParentVE: List<DetailParentVE>) : UiState()
        object Error : UiState()
        object Empty : UiState()
    }
}