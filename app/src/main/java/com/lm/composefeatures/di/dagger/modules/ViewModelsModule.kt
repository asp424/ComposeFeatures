package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.line.ui.ViewModels
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ViewModelsModule {

    @Binds
    @Singleton
    fun bindsViewModels(viewModels: ViewModels.Base): ViewModels
}