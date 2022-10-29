package com.bianchini.vinicius.matheus.core.usecase

import androidx.paging.PagingConfig
import com.bianchini.vinicius.matheus.core.data.repository.CharactersRepository
import com.exemplo.testing.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @Before
    fun setup() {
        getCharacterUseCase = GetCharactersUseCaseImpl(charactersRepository)
    }

    @Test
    fun `should valida flow paging data creation when invoke from use case is called`() =
        runBlockingTest {
            val result = getCharacterUseCase.invoke(
                GetCharactersUseCase.GetCharactersParams(
                    "",
                    PagingConfig(20)
                )
            )



        }
}