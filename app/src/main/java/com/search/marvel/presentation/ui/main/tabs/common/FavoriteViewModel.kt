package com.search.marvel.presentation.ui.main.tabs.common

import androidx.lifecycle.viewModelScope
import com.search.marvel.Utils.asMutable
import com.search.marvel.domain.usecase.GetFavoriteListUseCase
import com.search.marvel.domain.usecase.SaveFavoriteListUseCase
import com.search.marvel.presentation.model.CharacterCardModel
import com.search.marvel.presentation.ui.LoadingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getFavoriteListUseCase: GetFavoriteListUseCase, private val saveFavoriteListUseCase: SaveFavoriteListUseCase
) : LoadingViewModel() {
    val favoriteListFlow: StateFlow<List<CharacterCardModel>> = MutableStateFlow(emptyList())

    init {
        viewModelScope.launch {
            favoriteListFlow.asMutable().emit(getFavoriteListUseCase())
        }
    }

    fun saveFavoriteList() {
        saveFavoriteListUseCase.invoke(favoriteListFlow.value)
    }

    fun updateFavoriteList(item: CharacterCardModel): List<CharacterCardModel> {
        val result = favoriteListFlow.value.toMutableList().apply {
            if (!item.isFavorite) {
                remove(item)
            } else {
                add(size, item)
            }
        }.let {
            if (it.size > FAVORITE_LIST_MAX_SIZE) it.subList(it.size - FAVORITE_LIST_MAX_SIZE, it.size) else it
        }

        return result.also {
            viewModelScope.launch {
                favoriteListFlow.asMutable().emit(result)
            }
        }
    }

    companion object {
        private const val FAVORITE_LIST_MAX_SIZE = 5
    }
}