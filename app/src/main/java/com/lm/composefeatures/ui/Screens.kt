package com.lm.composefeatures.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import com.lm.composefeatures.custom_slider.Figures
import com.lm.composefeatures.custom_slider.MainScreenHandler
import com.lm.composefeatures.di.compose.ComposeDependencies
import com.lm.composefeatures.presentation.ViewModels
import kotlinx.coroutines.delay
import javax.inject.Inject

interface Screens {

    @Composable
    fun CustomSlider(radius: Float, distance: Float, figure: Figures)

    class Base @Inject constructor(
        private val mainScreenHandler: MainScreenHandler
    ) : Screens {

        @Composable
        override fun CustomSlider(radius: Float, distance: Float, figure: Figures) {

            with(mainScreenHandler) {

                    InitListPoints(figure)

                    BoxWithCanvas(figure, radius)

                    CheckForStrike(radius, distance, figure)

                    AutoMoveBall()

                    Debug()
            }
        }
    }
}
