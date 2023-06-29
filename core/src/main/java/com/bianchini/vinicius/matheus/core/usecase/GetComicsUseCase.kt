package com.bianchini.vinicius.matheus.core.usecase

import com.bianchini.vinicius.matheus.core.data.repository.CharactersRepository
import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.bianchini.vinicius.matheus.core.usecase.base.ResultStatus
import com.bianchini.vinicius.matheus.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetComicsUseCase {

    operator fun invoke(params: GetComicsParams): Flow<ResultStatus<List<Comic>>>

    data class GetComicsParams(val characterId: Int)
}

class GetComicsUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository
) : GetComicsUseCase, UseCase<GetComicsUseCase.GetComicsParams, List<Comic>>() {

    override suspend fun doWork(
        params: GetComicsUseCase.GetComicsParams
    ): ResultStatus<List<Comic>> {
        val comics = charactersRepository.getComics(params.characterId)

        return ResultStatus.Success(comics)
    }

}