package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.ui.custom_slider.CustomSliderHandler
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface CustomSliderHandlerModule {

    @Binds
    @Singleton
    fun bindsCustomSliderHandler(customSliderHandler: CustomSliderHandler.Base): CustomSliderHandler
}