package com.bianchini.vinicius.matheus.core.usecase

import com.bianchini.vinicius.matheus.core.data.repository.character.CharactersRepository
import com.bianchini.vinicius.matheus.core.usecase.base.ResultStatus
import com.exemple.testing.MainCoroutineRule
import com.exemple.testing.model.CharacterFactory
import com.exemple.testing.model.ComicFactory
import com.exemple.testing.model.EventFactory
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
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
class GetCharacterCategoriesUseCaseImplTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var getCharacterCategoriesUseCase: GetCharacterCategoriesUseCase

    @Mock
    private lateinit var repository: CharactersRepository

    private val character = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)
    private val comics = listOf(ComicFactory().create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory().create(EventFactory.FakeEvent.FakeEvent1))

    @Before
    fun setUp() {
        getCharacterCategoriesUseCase = GetCharacterCategoriesUseCaseImpl(
            repository,
            mainCoroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `should return Success from ResultStatus when get both requests return success`() =
        runTest {
            // Arrange
            whenever(repository.getComics(character.id)).thenReturn(comics)
            whenever(repository.getEvents(character.id)).thenReturn(events)

            // Act
            val result = getCharacterCategoriesUseCase.invoke(
                GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(character.id)
            )

            // Assert
            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0]) // Loading
            assertTrue(resultList[1] is ResultStatus.Success)
        }

    @Test
    fun `should return Error from ResultStatus when get events request returns error`() =
        runTest {
            // Arrange
            whenever(repository.getEvents(character.id)).thenAnswer {
                throw Throwable()
            }

            // Act
            val result = getCharacterCategoriesUseCase.invoke(
                GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(character.id)
            )

            // Assert
            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0]) // Loading
            assertTrue(resultList[1] is ResultStatus.Error)
        }

    @Test
    fun `should return Error from ResultStatus when get comics request returns error`() =
        runTest {
            // Arrange
            whenever(repository.getComics(character.id)).thenReturn(comics)
            whenever(repository.getComics(character.id)).thenAnswer {
                throw Throwable()
            }

            // Act
            val result = getCharacterCategoriesUseCase.invoke(
                GetCharacterCategoriesUseCase.GetCharacterCategoriesParams(character.id)
            )

            // Assert
            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0]) // Loading
            assertTrue(resultList[1] is ResultStatus.Error)
        }
}