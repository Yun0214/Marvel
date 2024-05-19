package com.search.marvel.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.search.marvel.data.Keys
import com.search.marvel.entity.SearchResultEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveFavoriteListByJson(newList: List<SearchResultEntity>) {
        runCatching {
            Gson().toJson(newList, object : TypeToken<List<SearchResultEntity>>() {}.type)
        }.getOrNull()?.also {
            sharedPreferences.edit().putString(Keys.FAVORITE_LIST_JSON_ID, it).apply()
        }
    }

    fun getFavoriteListJson(): List<SearchResultEntity>? {
        return sharedPreferences.getString(Keys.FAVORITE_LIST_JSON_ID, null)?.let {
            runCatching {
                Gson().fromJson<List<SearchResultEntity>>(it, object : TypeToken<List<SearchResultEntity>>() {}.type)
            }.getOrNull()
        }
    }
}