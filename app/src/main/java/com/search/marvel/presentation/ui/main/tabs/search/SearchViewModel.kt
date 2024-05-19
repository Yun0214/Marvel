package com.search.marvel.presentation.ui.main.tabs.search

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.search.marvel.Utils.asMutable
import com.search.marvel.presentation.model.CharacterCardModel
import com.search.marvel.presentation.model.Page
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    val searchResultFlow: StateFlow<Page<CharacterCardModel>?> = MutableStateFlow(null)
    val searchTextWatcherFlow: StateFlow<String?> = MutableStateFlow(null)
    val loadingFlow: StateFlow<Boolean> = MutableStateFlow(false)

    //todo - 추후 제거
    val tempFavoriteList = mutableListOf<CharacterCardModel>()

    fun emitToTextWatcherFlow(editable: Editable?) {
        viewModelScope.launch {
            searchTextWatcherFlow.asMutable().emit(editable?.trim()?.takeIf { it.length > 1 }?.toString())
        }
    }

    fun search(keyword: String) = viewModelScope.launch {
        delay(SEARCH_DELAY_MS)
        doWithLoading {
            test(keyword, 0).collectLatest {
                searchResultFlow.asMutable().emit(null)
                searchResultFlow.asMutable().emit(it)
            }
        }
    }

    fun callNextPage() = viewModelScope.launch {
        searchResultFlow.value?.also {
            doWithLoading {
                test(it.keyword, it.pageIndex + 1).collectLatest {
                    searchResultFlow.asMutable().emit(it)
                }
            }
        }
    }

    fun updateFavoriteList(item: CharacterCardModel): List<CharacterCardModel> {
        if (!item.isFavorite) {
            tempFavoriteList.remove(item)
        } else {
            tempFavoriteList.add(0, item)
            if (tempFavoriteList.size >= 5) return tempFavoriteList.subList(0, 5)
        }
        return tempFavoriteList
    }

    private suspend fun doWithLoading(block: suspend () -> Unit) {
        loadingFlow.asMutable().emit(true)
        block.invoke()
        loadingFlow.asMutable().emit(false)
    }

    fun clearSearchResultIfNeeded() {
        if (searchResultFlow.value != null) {
            viewModelScope.launch { searchResultFlow.asMutable().emit(null) }
        }
    }

    //todo - api 연동
    private fun test(keyword: String, page: Int) = flow {
        delay(1000L)
        mutableListOf<CharacterCardModel>().apply {
            repeat(10) {
                add(CharacterCardModel("hero$it ($page)",
                    "",
                    "test",
                    tempFavoriteList.find { favorite -> favorite.name == "hero$it ($page)" } != null))
            }
        }.run {
            emit(Page(keyword, page == 0, page, this))
        }
    }

    companion object {
        private const val SEARCH_DELAY_MS = 300L
    }
}