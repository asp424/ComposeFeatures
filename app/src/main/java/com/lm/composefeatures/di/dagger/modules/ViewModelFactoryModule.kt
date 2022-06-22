package com.lm.composefeatures.di.dagger.modules

import androidx.lifecycle.ViewModelProvider
import com.lm.composefeatures.line.ui.ViewModelFactory
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module(includes = [ViewModelModules::class])
interface ViewModelFactoryModule {

    @Binds
    @Singleton
    fun bindsViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

