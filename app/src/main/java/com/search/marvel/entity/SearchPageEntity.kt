package com.search.marvel.entity

data class SearchPageEntity(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<SearchResultEntity>,
    val total: Int
)