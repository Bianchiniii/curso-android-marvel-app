package com.bianchini.vinicius.matheus.core.usecase

import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRepository
import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.bianchini.vinicius.matheus.core.domain.model.Event
import com.bianchini.vinicius.matheus.core.usecase.base.CoroutinesDispatchers
import com.bianchini.vinicius.matheus.core.usecase.base.ResultStatus
import com.bianchini.vinicius.matheus.core.usecase.base.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetCharacterCategoriesUseCase {

    operator fun invoke(
        params: GetCharacterCategoriesParams
    ): Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>

    data class GetCharacterCategoriesParams(
        val characterId: Int
    )
}

class GetCharacterCategoriesUseCaseImpl @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val dispatchers: CoroutinesDispatchers
) : GetCharacterCategoriesUseCase,
    UseCase<GetCharacterCategoriesUseCase.GetCharacterCategoriesParams, Pair<List<Comic>, List<Event>>>() {

    override suspend fun doWork(
        params: GetCharacterCategoriesUseCase.GetCharacterCategoriesParams
    ): ResultStatus<Pair<List<Comic>, List<Event>>> {
        return withContext(dispatchers.io()) {
            val comicsDeferred = async { charactersRepository.getComics(params.characterId) }
            val eventsDeferred = async { charactersRepository.getEvents(params.characterId) }

            val comics = comicsDeferred.await()
            val events = eventsDeferred.await()

            ResultStatus.Success(comics to events)
        }
    }

}