package com.search.marvel.data.api

import com.search.marvel.BuildConfig
import com.search.marvel.data.repository.SearchRepository.Companion.CHARACTER_SEARCH_PAGING_COUNT
import com.search.marvel.entity.SearchResponseEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("characters")
    suspend fun searchCharacters(
        @Query("nameStartsWith") keyword: String,
        @Query("offset") offset: Int,
        @Query("ts") timeStampString: String,
        @Query("hash") hash: String,
        @Query("orderBy") orderBy: String = "name",
        @Query("limit") pagingSize: Int = CHARACTER_SEARCH_PAGING_COUNT,
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): Response<SearchResponseEntity>
}