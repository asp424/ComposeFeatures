package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.ui.custom_slider.HandlerUtils
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface HandlerUtilsModule {

    @Binds
    @Singleton
    fun bindsHandlerUtils(handlerUtils: HandlerUtils.Base): HandlerUtils
}