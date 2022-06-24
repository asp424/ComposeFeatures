package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.line.ui.main_screen.MainScreenHandler
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface MainScreenHandlerModule {

    @Binds
    @Singleton
    fun bindsMainScreenHandler(mainScreenHandler: MainScreenHandler.Base): MainScreenHandler
}