package com.search.marvel.presentation.model

data class CharacterCardModel(
    val name: String,
    val thumbnail: String,
    val description: String,
    var isFavorite: Boolean
)
