package com.lm.composefeatures.di.dagger

import com.lm.composefeatures.di.dagger.modules.*
import dagger.Module
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Module(
    includes = [
        ViewModelFactoryModule::class,
        CustomSliderHandlerModule::class,
        ScreensModule::class,
        ViewModelsModule::class,
        ComposeDependenciesModule::class,
        ComposeValuesModule::class,
        MoveBallModule::class,
        DebugWidgetsModule::class,
        CustomSliderHandlerHelperModule::class
    ]
)
interface AppMapModules

