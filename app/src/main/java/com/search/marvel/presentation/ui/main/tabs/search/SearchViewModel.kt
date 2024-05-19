package com.search.marvel.presentation.ui.main.tabs.search

import android.text.Editable
import androidx.lifecycle.viewModelScope
import com.search.marvel.Utils.asMutable
import com.search.marvel.domain.usecase.GetSearchResultUseCase
import com.search.marvel.presentation.model.CharacterCardModel
import com.search.marvel.presentation.model.Page
import com.search.marvel.presentation.ui.LoadingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val getSearchResultUseCase: GetSearchResultUseCase) : LoadingViewModel() {
    val searchResultFlow: StateFlow<Page<CharacterCardModel>?> = MutableStateFlow(null)
    val searchTextWatcherFlow: StateFlow<String?> = MutableStateFlow(null)

    var searchFailureFlow: SharedFlow<Boolean> = MutableSharedFlow()

    fun emitToTextWatcherFlow(editable: Editable?) {
        viewModelScope.launch {
            searchTextWatcherFlow.asMutable().emit(editable?.trim()?.takeIf { it.length > 1 }?.toString())
        }
    }

    fun search(keyword: String, favoriteList: List<CharacterCardModel>) = viewModelScope.launch {
        if (keyword != searchResultFlow.value?.keyword) {
            delay(SEARCH_DELAY_MS)
            doWithLoading {
                getSearchResultUseCase(keyword, 0, favoriteList)?.also {
                    searchResultFlow.asMutable().emit(null)
                    searchResultFlow.asMutable().emit(it)
                } ?: (searchFailureFlow as MutableSharedFlow).emit(true)
            }
        }
    }

    fun callNextPage(favoriteList: List<CharacterCardModel>) = viewModelScope.launch {
        searchResultFlow.value?.also {
            doWithLoading {
                getSearchResultUseCase(it.keyword, it.pageIndex + 1, favoriteList)?.also {
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

    companion object {
        private const val SEARCH_DELAY_MS = 300L
    }
}