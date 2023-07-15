package com.example.marvelapp.framework.di

import com.bianchini.vinicius.matheus.core.usecase.base.AppCoroutinesDispatchers
import com.bianchini.vinicius.matheus.core.usecase.base.CoroutinesDispatchers
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CoroutinesModule {

    @Binds
    fun bindDispatcher(dispatchers: AppCoroutinesDispatchers): CoroutinesDispatchers
}