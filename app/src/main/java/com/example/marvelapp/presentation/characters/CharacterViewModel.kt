package com.example.marvelapp.presentation.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bianchini.vinicius.matheus.core.usecase.GetCharactersUseCase
import com.bianchini.vinicius.matheus.core.usecase.base.CoroutinesDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    coroutinesDispatchers: CoroutinesDispatchers
) : ViewModel() {

    var currentSearchQuery = ""

    private val action = MutableLiveData<UiAction>()
    val state: LiveData<UiState> = action.switchMap { action ->
        when (action) {
            is UiAction.Search, UiAction.Sort -> {
                getCharactersUseCase(
                    GetCharactersUseCase.GetCharactersParams(
                        currentSearchQuery,
                        getPageConfig()
                    )
                ).cachedIn(viewModelScope).map {
                    UiState.SearchResult(it)
                }.asLiveData(coroutinesDispatchers.main())
            }
        }
    }

    init {
        searchCharacters()
    }

    private fun getPageConfig() = PagingConfig(PAGE_SIZE)

    fun searchCharacters() {
        action.postValue(UiAction.Search)
    }

    fun applySort() {
        action.postValue(UiAction.Sort)
    }

    fun closeSearch() {
        if (currentSearchQuery.isNotEmpty()) {
            currentSearchQuery = ""
        }
    }

    sealed class UiState {
        data class SearchResult(
            val data: PagingData<Character>
        ) : UiState()
    }

    sealed class UiAction {
        object Search : UiAction()

        object Sort : UiAction()
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}