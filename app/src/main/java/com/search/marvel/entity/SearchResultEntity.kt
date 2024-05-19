package com.search.marvel.entity

import java.io.Serializable

data class SearchResultEntity(
    val description: String,
    val id: Int,
    val name: String,
    val thumbnail: ThumbnailEntity,
    val thumbnailUrlString: String? = null
) : Serializable