package com.lm.composefeatures.di.dagger.modules

import com.lm.composefeatures.di.compose.ComposeValues
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ComposeValuesModule {

    @Binds
    @Singleton
    fun bindsComposeValues(composeValues: ComposeValues.Base): ComposeValues
}