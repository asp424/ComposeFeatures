package com.lm.composefeatures.ui.custom_slider

import androidx.compose.runtime.Composable
import javax.inject.Inject

interface Screens {

    @Composable
    fun CustomSlider()

    class Base @Inject constructor(
        private val mainScreenHandler: CustomSliderHandler,
        private val debugWidgets: DebugWidgets,
        private val moveBall: MoveBall
    ) : Screens {

        @Composable
        override fun CustomSlider() {
            debugWidgets.DrawDistance()
            with(mainScreenHandler) {
                InitListPoints()
                BoxWithCanvas()
                CheckForStrike()
            }
            moveBall.AutoMoveBallByTimer()
            debugWidgets.Debug()
        }
    }
}
