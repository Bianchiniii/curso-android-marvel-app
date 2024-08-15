package com.bianchini.vinicius.matheus.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bianchini.vinicius.matheus.core.data.repository.StorageRepository
import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRepository
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bianchini.vinicius.matheus.core.usecase.GetCharactersUseCase.GetCharactersParams
import com.bianchini.vinicius.matheus.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

interface GetCharactersUseCase {
    operator fun invoke(params: GetCharactersParams): Flow<PagingData<Character>>

    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)
}

class GetCharactersUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val storageRepository: StorageRepository,
) : PagingUseCase<GetCharactersParams, Character>(), GetCharactersUseCase {

    override fun createFlowObservable(params: GetCharactersParams): Flow<PagingData<Character>> {
        val orderBy = runBlocking { storageRepository.sorting.first() }

        return charactersRepository.getCachedCharacters(
            params.query,
            orderBy,
            params.pagingConfig
        )
    }
}