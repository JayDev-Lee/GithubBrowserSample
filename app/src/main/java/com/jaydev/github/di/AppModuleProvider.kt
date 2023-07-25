package com.jaydev.github.di

import android.content.ContentResolver
import android.content.Context
import com.jaydev.github.di.loader.ResourcesProvider
import com.jaydev.github.di.loader.ResourcesProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModuleProvider {

    @Provides
    @Singleton
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideResourceProvider(
        @ApplicationContext context: Context
    ): ResourcesProvider {
        return ResourcesProviderImpl(context)
    }
}