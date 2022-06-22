package com.lm.composefeatures.line.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import javax.inject.Inject

interface ViewModels {

    @Composable
    fun mainViewModel(): MainViewModel

    class Base @Inject constructor(
        private val viewModelFactory: ViewModelFactory
    ) : ViewModels {

        @Composable
        override fun mainViewModel(): MainViewModel = viewModel(factory = viewModelFactory)
    }
}
