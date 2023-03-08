package com.example.marvelapp.presentation.characters

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelapp.framework.di.BaseUrlModule
import com.example.marvelapp.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(BaseUrlModule::class)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CharactersFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        launchFragmentInHiltContainer<CharactersFragment>()
    }

    @Test
    fun shouldShowCharacter_whenViewIsCreated() {

    }
}