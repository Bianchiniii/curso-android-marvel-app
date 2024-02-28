package com.example.marvelapp.presentation.details

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bianchini.vinicius.matheus.core.usecase.AddFavoriteUseCase
import com.bianchini.vinicius.matheus.core.usecase.GetCharacterCategoriesUseCase
import com.bianchini.vinicius.matheus.core.usecase.base.CoroutinesDispatchers
import com.example.marvelapp.R
import com.example.marvelapp.utils.watchStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    coroutinesDispatchers: CoroutinesDispatchers
) : ViewModel() {

    val categories = UiActionStateStateFlow(
        coroutinesDispatchers.main(),
        getCharacterCategoriesUseCase
    )

    private val _favoriteUiStateFlow = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val favoriteUiState: StateFlow<FavoriteUiState> get() = _favoriteUiStateFlow

    fun getCharactersCategories(characterId: Int) = categories.load(characterId)


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

    sealed class FavoriteUiState {
        object Loading : FavoriteUiState()
        class FavoriteIcon(@DrawableRes val icon: Int) : FavoriteUiState()
    }
}