package com.lm.composefeatures.line.ui

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Rect
import com.lm.composefeatures.line.ui.main_screen.MainScreenHandler
import javax.inject.Inject

interface Screens {

    @Composable
    fun MainScreen()

    class Base @Inject constructor(
        private val mainScreenHandler: MainScreenHandler,
        private val viewModels: ViewModels,
    ) : Screens {

        @Composable
        override fun MainScreen() {
            var ballX by remember { mutableStateOf(0f) }
            var strike by remember { mutableStateOf(false) }
            var eventX by remember { mutableStateOf(0f) }
            var eventY by remember { mutableStateOf(0f) }
            val radius by remember { mutableStateOf(50f) }
            val listPoints = remember { mutableStateListOf<Rect>() }
            var action by remember { mutableStateOf(-1) }

            with(mainScreenHandler) {
                InitListPoints(listPoints, ballX, radius * 3) { ballX = it }

                MotionEventHandler(action, radius, eventX, eventY, ballX) { strike = it }

                MoveBall(listPoints, eventX, eventY, ballX, strike) { ballX = it }

                BoxWithCanvas(listPoints, radius, ballX, false) { mAction, mX, mY ->
                    action = mAction; eventX = mX; eventY = mY
                }
            }
        }
    }
}
