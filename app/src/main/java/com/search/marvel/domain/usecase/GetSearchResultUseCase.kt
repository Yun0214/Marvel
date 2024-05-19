package com.search.marvel.domain.usecase

import com.search.marvel.data.repository.SearchRepository
import com.search.marvel.data.repository.SearchRepository.Companion.CHARACTER_SEARCH_PAGING_COUNT
import com.search.marvel.entity.SearchResultEntity
import com.search.marvel.presentation.model.CharacterCardModel
import com.search.marvel.presentation.model.Page
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchResultUseCase @Inject constructor(private val searchRepository: SearchRepository) {
    suspend operator fun invoke(keyword: String, page: Int, favoriteList: List<CharacterCardModel>): Page<CharacterCardModel>? {
        return searchRepository.getSearchCharacterPaging(keyword, page)?.let {
            Page(
                keyword,
                it.total > (page * CHARACTER_SEARCH_PAGING_COUNT).plus(it.count) && it.results.isNotEmpty(),
                page,
                it.results.map { result ->
                    result.convertToModel(favoriteList.find { favorite -> favorite.id == result.id } != null)
                }
            )
        }
    }

    private fun SearchResultEntity.convertToModel(isFavorite: Boolean) = CharacterCardModel(
        id, name, thumbnail.let { "${it.path}.${it.extension}" }, description, isFavorite
    )
}