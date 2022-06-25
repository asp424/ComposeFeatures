package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.ui.custom_slider.Screens
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ScreensModule {

    @Binds
    @Singleton
    fun bindsScreens(screens: Screens.Base): Screens
}