package com.search.marvel.entity

data class SearchResultEntity(
    val description: String,
    val id: Int,
    val name: String,
    val thumbnail: ThumbnailEntity
)