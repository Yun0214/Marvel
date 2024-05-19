package com.search.marvel.presentation.ui.main.tabs.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.search.marvel.Utils.repeatOnLifecycleStart
import com.search.marvel.databinding.FragmentSearchBinding
import com.search.marvel.presentation.model.CharacterCardModel
import com.search.marvel.presentation.ui.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BindingFragment<FragmentSearchBinding>() {
    override val binding: FragmentSearchBinding by lazy { FragmentSearchBinding.inflate(layoutInflater) }

    private val vm: SearchViewModel by activityViewModels<SearchViewModel>()

    private var searchJob: Job? = null
    private var isEndPage = false

    override fun initPage(savedInstanceState: Bundle?) {
        initSearchInputField()
        initSearchResultList()

        observeTextWatcherFlow()
        observeSearchResultFlow()
        observeLoadingFlow()
    }

    private fun initSearchInputField() {
        with(binding.searchInputLayout) {
            setEndIconOnClickListener {
                if (editText?.text?.isNotBlank() == true) {
                    clearSearchResult()
                    editText?.setText("")
                }
            }
            editText?.run {
                doAfterTextChanged(vm::emitToTextWatcherFlow)
                setOnEditorActionListener { _, i, keyEvent ->
                    if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if (text.isBlank()) clearSearchResult()
                        activity?.also { hideSoftKeyboard(it) }
                    }
                    return@setOnEditorActionListener true
                }
            }
        }
    }

    private fun initSearchResultList() {
        with(binding.list) {
            adapter = CharacterCardListAdapter(object : CharacterCardListAdapter.ItemClickListener {
                override fun onItemClick(item: CharacterCardModel) {
                    (binding.list.adapter as CharacterCardListAdapter).updateFavoriteState(vm.updateFavoriteList(item))
                }
            })

            setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY - oldScrollY > 0) {
                    (layoutManager as GridLayoutManager).let {
                        if (isEndPage.not() && it.findLastVisibleItemPosition() >= (adapter?.itemCount ?: -1) - 2) {
                            isEndPage = true
                            lifecycleScope.launch {
                                vm.callNextPage()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun clearSearchResult() {
        if (isVisible) {
            vm.clearSearchResultIfNeeded()
            binding.emptyGroup.isVisible = false
            isEndPage = false
        }
    }

    private fun observeSearchResultFlow() {
        repeatOnLifecycleStart {
            vm.searchResultFlow.collectLatest { page ->
                if (isVisible) {
                    (binding.list.adapter as? CharacterCardListAdapter)?.also {
                        if (page != null) {
                            isEndPage = !page.hasNextPage
                            it.updateList(page)
                        } else {
                            it.submitList(emptyList())
                        }
                    }
                }
            }
        }
    }

    private fun observeTextWatcherFlow() {
        repeatOnLifecycleStart {
            vm.searchTextWatcherFlow.collectLatest {
                searchJob?.cancel()
                searchJob = it?.let { vm.search(it) }
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

    private fun hideSoftKeyboard(activity: Activity) {
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also { inputMethodManager ->
            if (inputMethodManager.isAcceptingText) {
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
                activity.currentFocus?.clearFocus()
            }
        }
    }
}