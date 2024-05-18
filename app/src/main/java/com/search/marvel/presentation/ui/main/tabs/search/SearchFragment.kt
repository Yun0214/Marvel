package com.search.marvel.presentation.ui.main.tabs.search

import android.os.Bundle
import com.search.marvel.databinding.FragmentSearchBinding
import com.search.marvel.presentation.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BindingFragment<FragmentSearchBinding>() {
    override val binding: FragmentSearchBinding by lazy { FragmentSearchBinding.inflate(layoutInflater) }

    override fun initPage(savedInstanceState: Bundle?) {

    }
}