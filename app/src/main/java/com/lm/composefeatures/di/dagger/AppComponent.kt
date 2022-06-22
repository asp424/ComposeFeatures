package com.lm.composefeatures.di.dagger

import com.lm.composefeatures.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppMapModules::class])
@Singleton
interface AppComponent {


    fun inject(mainActivity: MainActivity)
}