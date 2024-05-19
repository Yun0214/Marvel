package com.search.marvel.presentation.ui.main

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.search.marvel.databinding.ActivityMainBinding
import com.search.marvel.presentation.model.MainTabType
import com.search.marvel.presentation.ui.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>() {
    override val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainTabs by lazy { MainTabType.entries }

    override fun initPage(savedInstanceState: Bundle?) {
        initTabPage(savedInstanceState)
        initTabLayout()
    }

    private fun initTabPage(savedInstanceState: Bundle?) {
        with(binding.tabPager) {
            adapter = MainTabAdapter(this@MainActivity, mainTabs, savedInstanceState)
        }
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabs, binding.tabPager) { tabLayout, position ->
            runCatching {
                tabLayout.text = getString(mainTabs[position].nameId)
            }
        }.attach()
    }
}