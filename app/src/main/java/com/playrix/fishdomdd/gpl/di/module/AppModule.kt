package com.playrix.fishdomdd.gpl.di.module

import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO

    @Provides
    @Singleton
    fun provideAfLiveData() = MutableLiveData<MutableMap<String, Any>>()
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IODispatcher