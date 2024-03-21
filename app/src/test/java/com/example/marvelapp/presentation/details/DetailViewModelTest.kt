package com.example.marvelapp.presentation.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bianchini.vinicius.matheus.core.domain.model.Comic
import com.bianchini.vinicius.matheus.core.usecase.AddFavoriteUseCase
import com.bianchini.vinicius.matheus.core.usecase.GetCharacterCategoriesUseCase
import com.bianchini.vinicius.matheus.core.usecase.IsFavoriteUseCase
import com.bianchini.vinicius.matheus.core.usecase.RemoveFavoriteUseCase
import com.bianchini.vinicius.matheus.core.usecase.base.ResultStatus
import com.example.marvelapp.R
import com.exemple.testing.MainCoroutineRule
import com.exemple.testing.model.CharacterFactory
import com.exemple.testing.model.ComicFactory
import com.exemple.testing.model.EventFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    //regra utilizada para o liveData
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: DetailViewModel

    @Mock
    private lateinit var uiStateObserver: Observer<UiActionStateFlow.UiState>

    @Mock
    private lateinit var favoriteObserver: Observer<UiActionFavoriteStateFlow.UiState>

    private val character = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)
    private val comics = listOf(ComicFactory().create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory().create(EventFactory.FakeEvent.FakeEvent1))

    @Mock
    private lateinit var getCharactersCategoriesUseCase: GetCharacterCategoriesUseCase

    @Mock
    private lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @Mock
    private lateinit var isFavoriteUseCase: IsFavoriteUseCase

    @Mock
    private lateinit var removeFavoriteUseCase: RemoveFavoriteUseCase

    @Before
    fun setup() {
        viewModel = DetailViewModel(
            getCharactersCategoriesUseCase,
            addFavoriteUseCase,
            isFavoriteUseCase,
            removeFavoriteUseCase,
            mainCoroutineRule.testDispatcherProvider
        ).apply {
            categories.state.observeForever(uiStateObserver)
            favorite.state.observeForever(favoriteObserver)
        }
    }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns success`() =
        runTest {
            //arrange
            whenever(
                getCharactersCategoriesUseCase.invoke(any())
            ).thenReturn(
                flowOf(
                    ResultStatus.Success(
                        comics to events
                    )
                )
            )

            //act
            viewModel.categories.load(character.id)

            //assert
            verify(uiStateObserver).onChanged(isA<UiActionStateFlow.UiState.Success>())

            val uiStateSuccess =
                viewModel.categories.state.value as UiActionStateFlow.UiState.Success
            assertEquals(
                2,
                uiStateSuccess.detailParentVE.size
            )
            assertEquals(
                R.string.details_comics_category,
                uiStateSuccess.detailParentVE[0].categoryStringResId
            )
            assertEquals(
                R.string.details_events_category,
                uiStateSuccess.detailParentVE[1].categoryStringResId
            )
        }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns only comics`() =
        runTest {
            //arrange
            whenever(
                getCharactersCategoriesUseCase.invoke(any())
            ).thenReturn(
                flowOf(
                    ResultStatus.Success(
                        comics to emptyList()
                    )
                )
            )

            //act
            viewModel.categories.load(character.id)


            //assert
            verify(uiStateObserver).onChanged(isA<UiActionStateFlow.UiState>())

            val uiStateSuccess =
                viewModel.categories.state.value as UiActionStateFlow.UiState.Success
            assertEquals(
                1,
                uiStateSuccess.detailParentVE.size
            )
            assertEquals(
                R.string.details_comics_category,
                uiStateSuccess.detailParentVE[0].categoryStringResId
            )
        }

    @Test
    fun `should notify uiState with Success from UiState when get character categories returns only events`() =
        runTest {
            //arrange
            whenever(
                getCharactersCategoriesUseCase.invoke(any())
            ).thenReturn(
                flowOf(
                    ResultStatus.Success(
                        emptyList<Comic>() to events
                    )
                )
            )

            //act
            viewModel.categories.load(character.id)

            //assert
            verify(uiStateObserver).onChanged(isA<UiActionStateFlow.UiState>())
            val uiStateSuccess =
                viewModel.categories.state.value as UiActionStateFlow.UiState.Success
            assertEquals(
                1,
                uiStateSuccess.detailParentVE.size
            )
            assertEquals(
                R.string.details_events_category,
                uiStateSuccess.detailParentVE[0].categoryStringResId
            )
        }

    @Test
    fun `should notify uiState with Empty from UiState when get character categories returns an empty result list`() =
        runTest {
            //arrange
            whenever(
                getCharactersCategoriesUseCase.invoke(any())
            ).thenReturn(
                flowOf(
                    ResultStatus.Success(
                        emptyList<Comic>() to emptyList()
                    )
                )
            )

            //act
            viewModel.categories.load(character.id)

            //assert
            verify(uiStateObserver).onChanged(isA<UiActionStateFlow.UiState.Empty>())
        }

    @Test
    fun `should notify uiState with Error from UiState when get character categories returns an exception`() =
        runTest {
            //arrange
            whenever(
                getCharactersCategoriesUseCase.invoke(any())
            ).thenReturn(
                flowOf(
                    ResultStatus.Error(Throwable())
                )
            )

            //act
            viewModel.categories.load(character.id)

            //assert
            verify(uiStateObserver).onChanged(isA<UiActionStateFlow.UiState.Error>())
        }

    @Test
    fun `should notify favorite_uiState with filled favorite icon when check favorite returns true`() =
        runTest {
            // Arrange
            whenever(
                isFavoriteUseCase.invoke(any())
            ).thenReturn(
                flowOf(
                    ResultStatus.Success(true)
                )
            )

            // Act

            viewModel.favorite.isFavorite(character.id)

            // Assert
            verify(favoriteObserver).onChanged(
                isA<UiActionFavoriteStateFlow.UiState.Icon>()
            )

            val uiState = viewModel.favorite.state.value as UiActionFavoriteStateFlow.UiState.Icon

            assertEquals(R.drawable.ic_favorite_checked, uiState.icon)
        }

    @Test
    fun `should notify favorite_uiState with not filled favorite icon when check favorite returns false`() =
        runTest {
            // Arrange
            whenever(
                isFavoriteUseCase.invoke(any())
            ).thenReturn(
                flowOf(
                    ResultStatus.Success(false)
                )
            )

            // Act

            viewModel.favorite.isFavorite(character.id)

            // Assert
            verify(favoriteObserver).onChanged(
                isA<UiActionFavoriteStateFlow.UiState.Icon>()
            )

            val uiState = viewModel.favorite.state.value as UiActionFavoriteStateFlow.UiState.Icon

            assertEquals(R.drawable.ic_favorite_unchecked, uiState.icon)
        }
}