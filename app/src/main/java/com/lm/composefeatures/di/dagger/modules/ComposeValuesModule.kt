package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.di.compose.ComposeValues
import dagger.Binds
import dagger.Module

@Module
interface ComposeValuesModule {

    @Binds
    fun bindsComposeValues(composeValues: ComposeValues.Base): ComposeValues
}