package com.search.marvel.presentation.ui.main.tabs.common

import androidx.lifecycle.viewModelScope
import com.search.marvel.Utils.asMutable
import com.search.marvel.presentation.model.CharacterCardModel
import com.search.marvel.presentation.model.Page
import com.search.marvel.presentation.ui.LoadingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class FavoriteViewModel : LoadingViewModel() {
    val favoriteListFlow: StateFlow<List<CharacterCardModel>> = MutableStateFlow(emptyList())

    fun updateFavoriteList(item: CharacterCardModel) : List<CharacterCardModel> {
        val result = favoriteListFlow.value.toMutableList().apply {
            if (!item.isFavorite) {
                remove(item)
            } else {
                add(size, item)
            }
        }.let {
            if (it.size > 5) it.subList(it.size -5, it.size) else it
        }

        return result.also {
            viewModelScope.launch {
                favoriteListFlow.asMutable().emit(result)
            }
        }
    }
}