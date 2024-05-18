package com.search.marvel.presentation.ui.main.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.search.marvel.presentation.model.MainTabType

class MainTabAdapter(private val fragmentActivity: FragmentActivity, private val tabs: List<MainTabType>, private val savedInstanceState: Bundle?) :
    FragmentStateAdapter(fragmentActivity) {
    private val tabAndFragmentMap: MutableMap<MainTabType, Fragment> = mutableMapOf()

    private val manager
        get() = fragmentActivity.supportFragmentManager

    init {
        tabs.forEach {
            tabAndFragmentMap[it] = initOrCreateFragment(savedInstanceState, it)
        }
    }

    override fun getItemCount(): Int {
        return tabs.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabAndFragmentMap[tabs[position]]!!
    }

    private fun initOrCreateFragment(savedInstanceState: Bundle?, tab: MainTabType): Fragment {
        val savedFragment = if (savedInstanceState != null) manager.getFragment(savedInstanceState, fragmentActivity.getString(tab.nameId)) else null
        return savedFragment ?: tab.fragmentClazz.getDeclaredConstructor().newInstance() as Fragment
    }
}