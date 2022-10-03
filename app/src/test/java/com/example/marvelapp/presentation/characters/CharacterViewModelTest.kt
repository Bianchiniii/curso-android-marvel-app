package com.example.marvelapp.presentation.characters

import androidx.paging.PagingData
import com.bianchini.vinicius.matheus.core.domain.model.Character
import com.bianchini.vinicius.matheus.core.usecase.GetCharactersUseCase
import com.exemplo.testing.MainCoroutineRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterViewModelTest {

    //regra que inclue a dependencia do coroutines para inserir na thread principal e limpar após o fim
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    //dependencia de objeto -> unica forma de realizar testes com coroutines, flow, api ainda está
    //em desenvolvimento, podendo haver alterações
    //@ExperimentalCoroutinesApi
    //val testCoroutinesDispatchers: TestCoroutineDispatcher = TestCoroutineDispatcher()

    //não pode ser uma implementação final, deve ser uma abstração
    @Mock
    lateinit var getCharactersUseCase: GetCharactersUseCase

    private val pagingDataCharacter = PagingData.from(
        listOf(
            Character(
                "3-D Man",
                "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784"
            )
        )
    )

    //a classe que sera realiazada os testes jamais devera ser mockada
    private lateinit var characterViewModel: CharacterViewModel

    @ExperimentalCoroutinesApi
    //com a anotação @Before o metodo sera executado antes de cada teste
    @Before
    fun setUp() {
        //inicializa a classe que sera testada
        characterViewModel = CharacterViewModel(getCharactersUseCase)
    }

    @ExperimentalCoroutinesApi
    //teste a ser realizado
    @Test
    fun `should validate the paging data object values when calling characters`() =
        runBlockingTest {
            //quando o viewModel chamar o getCharactersUseCase.invoke
            whenever(getCharactersUseCase.invoke(any())).thenReturn(
                flowOf(
                    pagingDataCharacter
                )
            )

            //com o TestCoroutineDispatcher, executa o teste de forma sincrona, sem depender da execução
            //em outras thread, ele executa as tarefas IMEDIATAMENTE
            val result = characterViewModel.charactersPagingData("")

            assertEquals(1, result.count())
        }

    @ExperimentalCoroutinesApi
    @Test(expected = RuntimeException::class)
    fun `should throw an exception whe the calling to the use case returns an exception`() =
        runBlockingTest {
            whenever(getCharactersUseCase.invoke(any())).thenThrow(RuntimeException())

            characterViewModel.charactersPagingData("")
        }
}