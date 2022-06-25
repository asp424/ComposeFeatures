package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.custom_slider.DebugWidgets
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DebugWidgetsModule {

    @Binds
    @Singleton
    fun bindsDebugWidgets(debugWidgets: DebugWidgets.Base): DebugWidgets
}