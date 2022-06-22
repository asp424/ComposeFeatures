package com.lm.composefeatures.line.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject

interface ViewModels {

    fun mainViewModel(targetViewModelStoreOwner: ViewModelStoreOwner): MainViewModel

    class Base @Inject constructor(
        private val viewModelFactory: ViewModelFactory
    ) : ViewModels {

        override fun mainViewModel(targetViewModelStoreOwner: ViewModelStoreOwner): MainViewModel
        = ViewModelProvider(targetViewModelStoreOwner, viewModelFactory)[MainViewModel::class.java]
    }
}
