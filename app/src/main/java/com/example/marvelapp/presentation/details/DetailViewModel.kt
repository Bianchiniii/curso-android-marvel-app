package com.example.marvelapp.presentation.details

import androidx.lifecycle.ViewModel
import com.bianchini.vinicius.matheus.core.usecase.AddFavoriteUseCase
import com.bianchini.vinicius.matheus.core.usecase.GetCharacterCategoriesUseCase
import com.bianchini.vinicius.matheus.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase,
    addFavoriteUseCase: AddFavoriteUseCase,
    coroutinesDispatchers: CoroutinesDispatchers
) : ViewModel() {

    val categories = UiActionStateFlow(
        coroutinesDispatchers.main(),
        getCharacterCategoriesUseCase
    )

    val favorite = UiActionFavoriteStateFlow(
        coroutinesDispatchers.main(),
        addFavoriteUseCase
    )
}