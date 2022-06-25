package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.ui.custom_slider.MoveBall
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface MoveBallModule {

    @Binds
    @Singleton
    fun bindsMoveBall(moveBall: MoveBall.Base): MoveBall
}