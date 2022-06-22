package com.lm.composefeatures.di.dagger

import com.lm.composefeatures.di.dagger.modules.*
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Module(
    includes = [
        ViewModelFactoryModule::class,
        MainScreenHandlerModule::class,
        ScreensModule::class,
        ViewModelsModule::class,
        ComposeDependenciesModule::class,
        ComposeValuesModule::class
    ]
)
interface AppMapModules

