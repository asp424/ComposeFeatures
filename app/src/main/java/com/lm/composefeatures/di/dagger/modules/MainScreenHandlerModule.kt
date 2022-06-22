package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.line.ui.main_screen.MainScreenHandler
import dagger.Binds
import dagger.Module

@Module
interface MainScreenHandlerModule {

    @Binds
    fun bindsMainScreenHandler(mainScreenHandler: MainScreenHandler.Base): MainScreenHandler
}