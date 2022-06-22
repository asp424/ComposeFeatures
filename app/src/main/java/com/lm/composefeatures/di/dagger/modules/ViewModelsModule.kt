package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.line.ui.ViewModels
import dagger.Binds
import dagger.Module

@Module
interface ViewModelsModule {

    @Binds
    fun bindsViewModels(viewModels: ViewModels.Base): ViewModels
}