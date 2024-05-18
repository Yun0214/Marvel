package com.search.marvel.presentation.ui.main.tabs.favorite

import android.os.Bundle
import com.search.marvel.databinding.FragmentFavoriteBinding
import com.search.marvel.presentation.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BindingFragment<FragmentFavoriteBinding>() {
    override val binding: FragmentFavoriteBinding by lazy { FragmentFavoriteBinding.inflate(layoutInflater) }

    override fun initPage(savedInstanceState: Bundle?) {

    }
}