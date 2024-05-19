package com.search.marvel

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

object Utils {

    fun String.toMD5(): String {
        val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun View.setOnAntiDoubleClickListener(delayMs: Long = 300L, doClickEvent: (view: View) -> Unit) {
        var isClicked = false
        setOnClickListener {
            if (!isClicked) {
                isClicked = true
                doClickEvent(it)
                this.postDelayed({ isClicked = false }, delayMs)
            }
        }
    }

    fun LifecycleOwner.repeatOnLifecycleStart(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    fun LifecycleOwner.repeatOnLifecycleResume(block: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED, block)
        }
    }

    fun <T> StateFlow<T>.asMutable() = this as MutableStateFlow
}