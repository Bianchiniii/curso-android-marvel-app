package com.bianchini.vinicius.matheus.core.usecase

import com.bianchini.vinicius.matheus.core.data.repository.favorite.FavoritesRepository
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bianchini.vinicius.matheus.core.usecase.base.CoroutinesDispatchers
import com.bianchini.vinicius.matheus.core.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetFavoritesUseCase {
    suspend operator fun invoke(params: Unit = Unit): Flow<List<Character>>
}

class GetFavoritesUseCaseImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val dispatchers: CoroutinesDispatchers
) : FlowUseCase<Unit, List<Character>>(), GetFavoritesUseCase {

    override suspend fun createFlowObservable(params: Unit): Flow<List<Character>> {
        return withContext(dispatchers.io()) {
            favoritesRepository.getAllFavorites()
        }
    }
}