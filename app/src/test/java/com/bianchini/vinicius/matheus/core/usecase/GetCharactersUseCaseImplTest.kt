package com.bianchini.vinicius.matheus.core.usecase

import androidx.paging.PagingConfig
import com.bianchini.vinicius.matheus.core.data.repository.CharactersRepository
import com.exemplo.testing.MainCoroutineRule
import com.exemplo.testing.model.CharacterFactory
import com.exemplo.testing.pagingsource.PagingSourceFactory
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCharactersUseCaseImplTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    //classe a ser realizado o teste - interface
    private lateinit var getCharacterUseCase: GetCharactersUseCase

    //mockito a hora que rodar o teste vai saber criar esse obj
    @Mock
    lateinit var charactersRepository: CharactersRepository

    private val hero = CharacterFactory().create(CharacterFactory.Companion.Hero.Alexandre)
    private val fakePagingSource = PagingSourceFactory().create(listOf(hero))

    @Before
    fun setup() {
        getCharacterUseCase = GetCharactersUseCaseImpl(charactersRepository)
    }

    @Test
    fun `should valida flow paging data creation when invoke from use case is called`() =
        runBlockingTest {

            whenever(charactersRepository.getCharacters("")).thenReturn(fakePagingSource)

            val result = getCharacterUseCase.invoke(
                GetCharactersUseCase.GetCharactersParams(
                    "",
                    PagingConfig(20)
                )
            )

            //used to check if some function is called
            // use TIMES
            verify(charactersRepository, times(1)).getCharacters("")

            assertNotNull(result.first())
        }
}