package com.bianchini.vinicius.matheus.core.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bianchini.vinicius.matheus.core.data.repository.CharactersRepository
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bianchini.vinicius.matheus.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val charactersRepository: CharactersRepository
) : PagingUseCase<GetCharactersUseCase.GetCharactersParams, Character>() {

    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)

    override fun createFlowObservable(params: GetCharactersParams): Flow<PagingData<Character>> {
        return Pager(params.pagingConfig) {
            //paging source
            charactersRepository.getCharacters(params.query)
        }.flow
    }
}