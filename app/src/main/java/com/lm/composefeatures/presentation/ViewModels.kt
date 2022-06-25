package com.lm.composefeatures.presentation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import javax.inject.Inject

interface ViewModels {

    fun viewModelProvider(viewModelStoreOwner: ViewModelStoreOwner): ViewModelProvider

    class Base @Inject constructor(
        private val viewModelFactory: ViewModelFactory
    ) : ViewModels {

        override fun viewModelProvider(viewModelStoreOwner: ViewModelStoreOwner) =
            ViewModelProvider(viewModelStoreOwner, viewModelFactory)
    }
}
