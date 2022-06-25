package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.ui.custom_slider.CustomSliderHandlerHelper
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface CustomSliderHandlerHelperModule {

    @Binds
    @Singleton
    fun bindsCustomSliderHandlerHelper(customSliderHandlerHelper: CustomSliderHandlerHelper.Base): CustomSliderHandlerHelper
}