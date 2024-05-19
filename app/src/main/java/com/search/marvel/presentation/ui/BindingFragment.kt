package com.search.marvel.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.search.marvel.presentation.ui.common.LoadingDialog

abstract class BindingFragment<T : ViewBinding> : Fragment() {
    abstract val binding: T

    private var loadingDialog: LoadingDialog? = null

    abstract fun initPage(savedInstanceState: Bundle?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPage(savedInstanceState)
    }

    protected fun showLoading() {
        if (loadingDialog?.isShowing != true) {
            if (loadingDialog != null) hideLoading()
            context?.let {
                loadingDialog = LoadingDialog(it)
                loadingDialog?.show()
            }
        }
    }

    protected fun hideLoading() {
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.hide()
        }
        loadingDialog = null
    }
}