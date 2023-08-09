package com.example.marvelapp.presentation.details

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bianchini.vinicius.matheus.core.usecase.AddFavoriteUseCase
import com.bianchini.vinicius.matheus.core.usecase.GetCharacterCategoriesUseCase
import com.example.marvelapp.R
import com.example.marvelapp.utils.watchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase
) : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> get() = _uiStateFlow

    private val _favoriteUiStateFlow = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val favoriteUiState: StateFlow<FavoriteUiState> get() = _favoriteUiStateFlow

    fun getCharactersCategories(characterId: Int) = viewModelScope.launch {
        getCharacterCategoriesUseCase(
            GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(characterId)
        ).watchStatus(
            loading = {
                _uiStateFlow.value = UiState.Loading,
            },
            success = { data ->
                val detailsParentList = buildList {
                    val comics = data.first
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

                    val events = data.second
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

                _uiStateFlow.value = if (detailsParentList.isEmpty()) {
                    UiState.Empty
                } else UiState.Success(detailsParentList)

            },
            error = {
                _uiStateFlow.value = UiState.Error
            }

        )
    }

    fun addFavoriteCharacter(detailViewArg: DetailViewArg) = viewModelScope.launch {
        addFavoriteUseCase(
            AddFavoriteUseCase.AddFavoriteUseCaseParams(
                detailViewArg.id,
                detailViewArg.name,
                detailViewArg.imageUrl
            )
        ).watchStatus(
            loading = {
                _favoriteUiStateFlow.value = FavoriteUiState.Loading
            },
            success = {
                _favoriteUiStateFlow.value = FavoriteUiState.FavoriteIcon(
                    R.drawable.ic_favorite_checked
                )
            },
            error = {

            }
        )
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val detailParentVE: List<DetailParentVE>) : UiState()
        object Error : UiState()
        object Empty : UiState()
    }

    sealed class FavoriteUiState {
        object Loading : FavoriteUiState()
        class FavoriteIcon(@DrawableRes val icon: Int) : FavoriteUiState()
    }
}