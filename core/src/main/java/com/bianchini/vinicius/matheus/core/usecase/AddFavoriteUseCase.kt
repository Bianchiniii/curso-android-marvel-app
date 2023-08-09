package com.bianchini.vinicius.matheus.core.usecase

import com.bianchini.vinicius.matheus.core.data.repository.favorite.FavoritesRepository
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bianchini.vinicius.matheus.core.usecase.base.AppCoroutinesDispatchers
import com.bianchini.vinicius.matheus.core.usecase.base.CoroutinesDispatchers
import com.bianchini.vinicius.matheus.core.usecase.base.ResultStatus
import com.bianchini.vinicius.matheus.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AddFavoriteUseCase {

    operator fun invoke(
        params: AddFavoriteUseCaseParams
    ): Flow<ResultStatus<Unit>>

    data class AddFavoriteUseCaseParams(
        val characterId: Int,
        val name: String,
        val imageUrl: String
    )
}

class AddFavoriteUseCaseImpl @Inject constructor(
    private val favoriteRepository: FavoritesRepository,
    private val dispatchers: CoroutinesDispatchers
) :
    UseCase<AddFavoriteUseCase.AddFavoriteUseCaseParams, Unit>(), AddFavoriteUseCase {

    override suspend fun doWork(params: AddFavoriteUseCase.AddFavoriteUseCaseParams): ResultStatus<Unit> {
        return with(dispatchers.io()) {
            favoriteRepository.saveFavorite(
                Character(
                    params.characterId,
                    params.name,
                    params.imageUrl
                )
            )

            ResultStatus.Success(Unit)
        }
    }
}