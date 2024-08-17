package com.example.marvelapp.presentation.characters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.bianchini.vinicius.matheus.core.usecase.GetCharactersUseCase
import com.exemple.testing.MainCoroutineRule
import com.exemple.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

//regra que inclue a dependencia do coroutines para inserir na thread principal e limpar após o fim
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharacterViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    //dependencia de objeto -> unica forma de realizar testes com coroutines, flow, api ainda está
    //em desenvolvimento, podendo haver alterações
    //@ExperimentalCoroutinesApi
    //val testCoroutinesDispatchers: TestCoroutineDispatcher = TestCoroutineDispatcher()

    //não pode ser uma implementação final, deve ser uma abstração
    @Mock
    lateinit var getCharactersUseCase: GetCharactersUseCase

    private val characterFactory: CharacterFactory = CharacterFactory()

    private val pagingDataCharacter = PagingData.from(
        listOf(
            characterFactory.create(CharacterFactory.Hero.ABomb),
            characterFactory.create(CharacterFactory.Hero.ThreeDMan)
        )
    )

    //a classe que sera realiazada os testes jamais devera ser mockada
    private lateinit var characterViewModel: CharacterViewModel

    //com a anotação @Before o metodo sera executado antes de cada teste
    @Before
    fun setUp() {
        //inicializa a classe que sera testada
        characterViewModel =
            CharacterViewModel(
                getCharactersUseCase,
                mainCoroutineRule.testDispatcherProvider
            )
    }

    //teste a ser realizado
    @Test
    fun `should validate the paging data object values when calling characters`() =
        runTest {
            //quando o viewModel chamar o getCharactersUseCase.invoke
            whenever(getCharactersUseCase.invoke(any())).thenReturn(
                flowOf(
                    pagingDataCharacter
                )
            )

            val observer = mock(Observer::class.java) as Observer<CharacterViewModel.UiState>
            characterViewModel.state.observeForever(observer)

            //com o TestCoroutineDispatcher, executa o teste de forma sincrona, sem depender da execução
            //em outras thread, ele executa as tarefas IMEDIATAMENTE
            characterViewModel.searchCharacters()


            val state = characterViewModel.state.value as CharacterViewModel.UiState.SearchResult

            assertNotNull(state.data)
        }

    @Test(expected = RuntimeException::class)
    fun `should throw an exception whe the calling to the use case returns an exception`() =
        runTest {
            whenever(getCharactersUseCase.invoke(any())).thenThrow(RuntimeException())

            val observer = mock(Observer::class.java) as Observer<CharacterViewModel.UiState>
            characterViewModel.state.observeForever(observer)

            characterViewModel.searchCharacters()
        }
}