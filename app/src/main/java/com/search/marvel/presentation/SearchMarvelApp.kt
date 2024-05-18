package com.search.marvel.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SearchMarvelApp : Application() {

    override fun onCreate() {
        super.onCreate()
        //다크모드 방지
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}