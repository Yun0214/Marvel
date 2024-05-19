package com.search.marvel.presentation.ui.main.tabs.search

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.search.marvel.Utils.asMutable
import com.search.marvel.presentation.model.CharacterCardModel
import com.search.marvel.presentation.model.Page
import com.search.marvel.presentation.ui.LoadingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SearchViewModel : LoadingViewModel() {
    val searchResultFlow: StateFlow<Page<CharacterCardModel>?> = MutableStateFlow(null)
    val searchTextWatcherFlow: StateFlow<String?> = MutableStateFlow(null)

    fun emitToTextWatcherFlow(editable: Editable?) {
        viewModelScope.launch {
            searchTextWatcherFlow.asMutable().emit(editable?.trim()?.takeIf { it.length > 1 }?.toString())
        }
    }

    fun search(keyword: String, favoriteList: List<CharacterCardModel>) = viewModelScope.launch {
        if (keyword != searchResultFlow.value?.keyword) {
            delay(SEARCH_DELAY_MS)
            doWithLoading {
                test(keyword, 0, favoriteList).collectLatest {
                    searchResultFlow.asMutable().emit(null)
                    searchResultFlow.asMutable().emit(it)
                }
            }
        }
    }

    fun callNextPage(favoriteList: List<CharacterCardModel>) = viewModelScope.launch {
        searchResultFlow.value?.also {
            doWithLoading {
                test(it.keyword, it.pageIndex + 1, favoriteList).collectLatest {
                    searchResultFlow.asMutable().emit(it)
                }
            }
        }
    }

    fun clearSearchResultIfNeeded() {
        if (searchResultFlow.value != null) {
            viewModelScope.launch { searchResultFlow.asMutable().emit(null) }
        }
    }

    //todo - api 연동
    private fun test(keyword: String, page: Int, favoriteList: List<CharacterCardModel>) = flow {
        delay(1000L)
        mutableListOf<CharacterCardModel>().apply {
            repeat(10) {
                add(CharacterCardModel("hero$it ($page)",
                    "",
                    "test",
                    favoriteList.find { favorite -> favorite.name == "hero$it ($page)" } != null))
            }
        }.run {
            emit(Page(keyword, page == 0, page, this))
        }
    }

    companion object {
        private const val SEARCH_DELAY_MS = 300L
    }
}