package com.search.marvel.di

import android.content.Context
import android.content.SharedPreferences
import com.search.marvel.data.Keys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Keys.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE)
    }
}
