package com.search.marvel.presentation.ui

import androidx.lifecycle.ViewModel
import com.search.marvel.Utils.asMutable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class LoadingViewModel : ViewModel() {
    val loadingFlow: StateFlow<Boolean> = MutableStateFlow(false)

    protected suspend fun doWithLoading(block: suspend () -> Unit) {
        loadingFlow.asMutable().emit(true)
        block.invoke()
        loadingFlow.asMutable().emit(false)
    }
}