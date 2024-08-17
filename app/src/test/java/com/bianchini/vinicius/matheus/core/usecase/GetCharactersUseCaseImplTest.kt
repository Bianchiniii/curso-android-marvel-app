package com.bianchini.vinicius.matheus.core.usecase

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bianchini.vinicius.matheus.core.data.repository.StorageRepository
import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRepository
import com.exemple.testing.MainCoroutineRule
import com.exemple.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
//THIS TEST SHOULD BE IN CORE PACKAGE
@RunWith(MockitoJUnitRunner::class)
class GetCharactersUseCaseImplTest {

    //regra que inclue a dependencia do coroutines para inserir na thread principal e limpar após o fim
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    //classe a ser realizado o teste - interface
    private lateinit var getCharacterUseCase: GetCharactersUseCase

    //mockito a hora que rodar o teste vai saber criar esse obj
    @Mock
    lateinit var charactersRepository: CharactersRepository

    @Mock
    lateinit var storageRepository: StorageRepository

    private val hero = CharacterFactory().create(CharacterFactory.Hero.ABomb)
    private val fakePagingData = PagingData.from(listOf(hero))

    @Before
    fun setup() {
        getCharacterUseCase = GetCharactersUseCaseImpl(charactersRepository, storageRepository)
    }

    @Test
    fun `should valida flow paging data creation when invoke from use case is called`() =
        runTest {
            val pagingConfig = PagingConfig(20)
            val orderBy = "ascendig"
            val query = "spider"
            whenever(
                charactersRepository.getCachedCharacters(
                    query,
                    orderBy,
                    pagingConfig
                )
            ).thenReturn(
                flowOf(fakePagingData)
            )

            whenever(
                storageRepository.sorting
            ).thenReturn(flowOf(orderBy))

            val result = getCharacterUseCase.invoke(
                GetCharactersUseCase.GetCharactersParams(
                    query,
                    pagingConfig
                )
            )

            //used to check if some function is called
            // use TIMES
            verify(charactersRepository, times(1)).getCachedCharacters(query, orderBy, pagingConfig)

            assertNotNull(result.first())
        }
}