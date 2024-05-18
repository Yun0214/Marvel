package com.search.marvel.presentation.ui.main

import android.os.Bundle
import com.search.marvel.databinding.ActivityMainBinding
import com.search.marvel.presentation.ui.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>() {
    override val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun initPage(savedInstanceState: Bundle?) {

    }
}