package com.search.marvel.domain.usecase

import com.search.marvel.data.repository.FavoriteRepository
import com.search.marvel.entity.SearchResultEntity
import com.search.marvel.entity.ThumbnailEntity
import com.search.marvel.presentation.model.CharacterCardModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveFavoriteListUseCase @Inject constructor(private val repository: FavoriteRepository) {
    operator fun invoke(newList: List<CharacterCardModel>) {
        repository.saveFavoriteListByJson(newList.map { it.convertToEntity() })
    }

    private fun CharacterCardModel.convertToEntity(): SearchResultEntity {
        return SearchResultEntity(
            description, id, name, ThumbnailEntity("", ""), thumbnail
        )
    }
}