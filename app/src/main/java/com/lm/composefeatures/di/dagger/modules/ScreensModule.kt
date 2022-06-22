package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.line.ui.Screens
import dagger.Binds
import dagger.Module

@Module
interface ScreensModule {

    @Binds
    fun bindsScreens(screens: Screens.Base): Screens
}