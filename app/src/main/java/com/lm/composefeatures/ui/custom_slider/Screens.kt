package com.lm.composefeatures.ui.custom_slider

import androidx.compose.runtime.Composable
import javax.inject.Inject

interface Screens {

    @Composable
    fun CustomSlider()

    class Base @Inject constructor(
        private val mainScreenHandler: MainScreenHandler
    ) : Screens {

        @Composable
        override fun CustomSlider() {
            with(mainScreenHandler) {
                InitListPoints()
                BoxWithCanvas()
                CheckForStrike()
                AutoMoveBall()
                Debug()
            }
        }
    }
}
