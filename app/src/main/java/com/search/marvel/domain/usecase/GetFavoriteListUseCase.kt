package com.search.marvel.domain.usecase

import com.search.marvel.data.repository.FavoriteRepository
import com.search.marvel.entity.SearchResultEntity
import com.search.marvel.presentation.model.CharacterCardModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavoriteListUseCase @Inject constructor(private val repository: FavoriteRepository) {

    operator fun invoke(): List<CharacterCardModel> {
        return repository.getFavoriteListJson()?.let { entities ->
            entities.map { it.convertToModel() }
        } ?: emptyList()
    }

    private fun SearchResultEntity.convertToModel(): CharacterCardModel {
        return CharacterCardModel(
            id, name, thumbnailUrlString ?: "", description, true
        )
    }
}