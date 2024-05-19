package com.search.marvel.data.repository

import android.util.Log
import com.search.marvel.BuildConfig
import com.search.marvel.Utils.toMD5
import com.search.marvel.data.api.SearchApi
import com.search.marvel.entity.SearchPageEntity
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val searchApiService: SearchApi) {

    suspend fun getSearchCharacterPaging(keyword: String, page: Int) : SearchPageEntity? {
        return withContext(Dispatchers.IO) {
            val timeStamp = System.currentTimeMillis()
            val hash = "$timeStamp${BuildConfig.SECRET_KEY}${BuildConfig.API_KEY}".toMD5()
            runCatching {
                searchApiService.searchCharacters(keyword, page * CHARACTER_SEARCH_PAGING_COUNT, timeStamp.toString(), hash).let { response ->
                    if (response.isSuccessful) {
                        response.body()?.searchPageData
                    } else null
                }
            }.getOrNull()
        }
    }

    companion object {
        const val CHARACTER_SEARCH_PAGING_COUNT = 10
    }
}