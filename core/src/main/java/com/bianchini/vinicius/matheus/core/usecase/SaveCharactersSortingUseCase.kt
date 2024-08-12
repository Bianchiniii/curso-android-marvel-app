package com.bianchini.vinicius.matheus.core.usecase

import com.bianchini.vinicius.matheus.core.data.mapper.SortingMapper
import com.bianchini.vinicius.matheus.core.data.repository.StorageRepository
import com.bianchini.vinicius.matheus.core.usecase.base.CoroutinesDispatchers
import com.bianchini.vinicius.matheus.core.usecase.base.ResultStatus
import com.bianchini.vinicius.matheus.core.usecase.base.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SaveCharactersSortingUseCase {

    operator fun invoke(
        params: SaveCharactersSortingUseCaseParams
    ): Flow<ResultStatus<Unit>>

    data class SaveCharactersSortingUseCaseParams(
        val pair: Pair<String, String>
    )
}

class SaveCharactersSortingUseCaseImpl @Inject constructor(
    private val storageRepository: StorageRepository,
    private val sortingMapper: SortingMapper,
    private val dispatchers: CoroutinesDispatchers
) :
    UseCase<SaveCharactersSortingUseCase.SaveCharactersSortingUseCaseParams, Unit>(),
    SaveCharactersSortingUseCase {

    override suspend fun doWork(
        params: SaveCharactersSortingUseCase.SaveCharactersSortingUseCaseParams
    ): ResultStatus<Unit> {
        return with(dispatchers.io()) {
            storageRepository.saveSorting(sortingMapper.mapFromPair(params.pair))

            ResultStatus.Success(Unit)
        }
    }
}