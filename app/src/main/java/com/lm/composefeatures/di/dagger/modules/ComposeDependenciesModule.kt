package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.di.compose.ComposeDependencies
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ComposeDependenciesModule {

    @Binds
    @Singleton
    fun bindsComposeDependencies(composeDependencies: ComposeDependencies.Base)
    : ComposeDependencies
}