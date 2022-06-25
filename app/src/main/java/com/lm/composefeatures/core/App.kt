package com.lm.composefeatures.core

import android.app.Application
import android.content.Context
import com.lm.composefeatures.di.dagger.AppComponent
import com.lm.composefeatures.di.dagger.DaggerAppComponent

class App: Application() {
    val appComponent = DaggerAppComponent.builder().build()


}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> (applicationContext as App).appComponent
    }