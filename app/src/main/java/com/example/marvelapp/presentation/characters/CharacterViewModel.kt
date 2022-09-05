package com.example.marvelapp.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bianchini.vinicius.matheus.core.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    fun charactersPagingData(queries: String): Flow<PagingData<Character>> {
        return getCharactersUseCase(
            GetCharactersUseCase.GetCharactersParams(
                queries,
                getPageConfig()
            )
        ).cachedIn(viewModelScope)
    }

    private fun getPageConfig() = PagingConfig(PAGE_SIZE)

    companion object {
        const val PAGE_SIZE = 20
    }
}