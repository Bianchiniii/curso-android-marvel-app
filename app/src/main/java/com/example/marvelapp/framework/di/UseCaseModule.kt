package com.example.marvelapp.framework.di

import com.bianchini.vinicius.matheus.core.usecase.GetCharactersUseCase
import com.bianchini.vinicius.matheus.core.usecase.GetCharactersUseCaseImpl
import com.bianchini.vinicius.matheus.core.usecase.GetComicsUseCase
import com.bianchini.vinicius.matheus.core.usecase.GetComicsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    fun bidsCharactersUseCase(useCaseImpl: GetCharactersUseCaseImpl): GetCharactersUseCase

    @Binds
    fun bindsComicsUseCase(useCaseImpl: GetComicsUseCaseImpl): GetComicsUseCase
}