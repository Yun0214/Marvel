package com.search.marvel.presentation.model

import com.search.marvel.R
import com.search.marvel.presentation.ui.main.tabs.favorite.FavoriteFragment
import com.search.marvel.presentation.ui.main.tabs.search.SearchFragment

enum class MainTabType(val nameId: Int, val fragmentClazz: Class<*>) {
    SEARCH(R.string.search_tab, SearchFragment::class.java),
    FAVORITE(R.string.favorite_tab, FavoriteFragment::class.java)
}