package com.lm.composefeatures.ui.custom_slider

import androidx.compose.runtime.Composable
import com.lm.composefeatures.di.compose.ComposeDependencies
import javax.inject.Inject

interface Screens {

    @Composable
    fun CustomSlider()

    class Base @Inject constructor(
        private val mainScreenHandler: CustomSliderHandler
    ) : Screens {

        @Composable
        override fun CustomSlider() {
            with(mainScreenHandler) {
                DrawDistance()
                InitListPoints()
                BoxWithCanvas()
                CheckForStrike()
                AutoMoveBall()
                Debug()
            }
        }
    }
}
