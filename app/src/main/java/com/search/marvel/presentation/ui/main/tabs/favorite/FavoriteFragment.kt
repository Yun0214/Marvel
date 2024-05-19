package com.search.marvel.presentation.ui.main.tabs.favorite

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.search.marvel.Utils.repeatOnLifecycleResume
import com.search.marvel.Utils.repeatOnLifecycleStart
import com.search.marvel.databinding.FragmentFavoriteBinding
import com.search.marvel.presentation.model.CharacterCardModel
import com.search.marvel.presentation.ui.BindingFragment
import com.search.marvel.presentation.ui.main.tabs.common.CharacterCardListAdapter
import com.search.marvel.presentation.ui.main.tabs.common.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FavoriteFragment : BindingFragment<FragmentFavoriteBinding>() {
    override val binding: FragmentFavoriteBinding by lazy { FragmentFavoriteBinding.inflate(layoutInflater) }

    private val vm: FavoriteViewModel by activityViewModels()

    override fun initPage(savedInstanceState: Bundle?) {
        initFavoriteList()

        observeFavoriteListFlow()
        observeLoadingFlow()
    }

    private fun initFavoriteList() {
        with(binding.list) {
            adapter = CharacterCardListAdapter(object : CharacterCardListAdapter.ItemClickListener {
                override fun onItemClick(item: CharacterCardModel) {
                    (binding.list.adapter as CharacterCardListAdapter).removeFavorite(item)
                    vm.updateFavoriteList(item)
                }
            })
        }
    }

    private fun observeFavoriteListFlow() {
        repeatOnLifecycleResume {
            vm.favoriteListFlow.collectLatest { list ->
                if (isVisible) {
                    (binding.list.adapter as? CharacterCardListAdapter)?.also {
                        it.submitList(list)
                        binding.emptyGroup.isVisible = list.isEmpty()
                    }
                }
            }
        }
    }

    private fun observeLoadingFlow() {
        repeatOnLifecycleStart {
            vm.loadingFlow.collectLatest {
                if (it) showLoading() else hideLoading()
            }
        }
    }
}