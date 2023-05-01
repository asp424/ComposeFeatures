package com.lm.composefeatures.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lm.composefeatures.core.appComponent
import com.lm.composefeatures.ui.custom_slider.Figures
import com.lm.composefeatures.di.compose.ComposeDependencies
import com.lm.composefeatures.ui.custom_slider.Screens
import com.lm.composefeatures.ui.pager.CircleRevealPager
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
            CircleRevealPager()
            /*composeDependencies.MainScreenDependencies(
                Figures.SINUS, 50f, 1000, 100f
            ) { screens.CustomSlider() }

             */
        }
    }
}
