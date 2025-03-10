package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import com.bianchini.vinicius.matheus.core.data.repository.CharactersRemoteDataSource
import com.example.marvelapp.factory.response.CharacterPagingResponseFactory
import com.exemple.testing.MainCoroutineRule
import com.exemple.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharactersPagingSourceTest {

    //regra que inclue a dependencia do coroutines para inserir na thread principal e limpar após o fim
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var charactersPagingSource: CharactersPagingSource

    private val dataWrapperResponse = CharacterPagingResponseFactory()

    @Mock
    lateinit var charactersRemoteDataSource: CharactersRemoteDataSource

    private val characterFactory = CharacterFactory()

    @Before
    fun setup() {
        charactersPagingSource = CharactersPagingSource(charactersRemoteDataSource, "")
    }

    @Test
    fun `should return a success load result when load is called`() = runTest {
        //arange
        whenever(charactersRemoteDataSource.fetchCharacters(any())).thenReturn(
            dataWrapperResponse.create()
        )

        //act
        val result = charactersPagingSource.load(
            PagingSource.LoadParams.Refresh(
                null,
                2,
                false
            )
        )

        //assert
        val expected = listOf(
            characterFactory.create(CharacterFactory.Hero.ThreeDMan),
            characterFactory.create(CharacterFactory.Hero.ABomb)
        )

        assertEquals(
            PagingSource.LoadResult.Page(
                data = expected,
                prevKey = null,
                nextKey = 20,
            ),
            result
        )
    }

    @Test
    fun `should return a error result when load is called`() = runTest {
        //arrange
        val runtimeException = RuntimeException()
        whenever(charactersRemoteDataSource.fetchCharacters(any())).thenThrow(
            runtimeException
        )

        //act
        val result = charactersPagingSource.load(
            PagingSource.LoadParams.Refresh(
                null,
                2,
                false
            )
        )

        //assert
        assertEquals(
            PagingSource.LoadResult.Error<Int, Character>(
                runtimeException
            ),
            result
        )
    }
}