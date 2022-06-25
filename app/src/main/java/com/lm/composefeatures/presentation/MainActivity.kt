package com.lm.composefeatures.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lm.composefeatures.core.appComponent
import com.lm.composefeatures.custom_slider.Figures
import com.lm.composefeatures.di.compose.ComposeDependencies
import com.lm.composefeatures.ui.Screens
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var screens: Screens

    @Inject
    lateinit var composeDependencies: ComposeDependencies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContent {
            composeDependencies.MainScreenDependencies {
                screens.CustomSlider(50f, 100f, Figures.SINUS)
            }
        }
    }
}
