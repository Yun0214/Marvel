package com.search.marvel.entity

import com.google.gson.annotations.SerializedName

data class SearchResponseEntity(
    @SerializedName("data")
    val searchPageData: SearchPageEntity,
)