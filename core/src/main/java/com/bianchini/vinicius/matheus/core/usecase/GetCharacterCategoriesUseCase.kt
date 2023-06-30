package com.bianchini.vinicius.matheus.core.usecase

import com.bianchini.vinicius.matheus.core.data.repository.CharactersRepository
import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.bianchini.vinicius.matheus.core.domain.model.Event
import com.bianchini.vinicius.matheus.core.usecase.base.AppCoroutinesDispatchers
import com.bianchini.vinicius.matheus.core.usecase.base.ResultStatus
import com.bianchini.vinicius.matheus.core.usecase.base.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetCharacterCategoriesUseCase {

    operator fun invoke(
        params: GetComicsParams
    ): Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>

    data class GetComicsParams(
        val characterId: Int
    )
}

class GetComicsUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val appCoroutinesDispatchers: AppCoroutinesDispatchers
) : GetCharacterCategoriesUseCase,
    UseCase<GetCharacterCategoriesUseCase.GetComicsParams, Pair<List<Comic>, List<Event>>>() {

    override suspend fun doWork(
        params: GetCharacterCategoriesUseCase.GetComicsParams
    ): ResultStatus<Pair<List<Comic>, List<Event>>> {
        return withContext(appCoroutinesDispatchers.io) {
            val comicsDeferred = async { charactersRepository.getComics(params.characterId) }
            val eventsDeferred = async { charactersRepository.getEvents(params.characterId) }

            val comics = comicsDeferred.await()
            val events = eventsDeferred.await()

            ResultStatus.Success(comics to events)
        }
    }

}