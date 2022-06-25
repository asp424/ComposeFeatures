package com.lm.composefeatures.ui

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import com.lm.composefeatures.custom_slider.Figures
import com.lm.composefeatures.custom_slider.MainScreenHandler
import com.lm.composefeatures.presentation.ViewModels
import javax.inject.Inject

interface Screens {

    @Composable
    fun CustomSlider()

    class Base @Inject constructor(
        private val mainScreenHandler: MainScreenHandler,
        private val viewModels: ViewModels
    ) : Screens {

        @Composable
        override fun CustomSlider() {
            var offset by remember { mutableStateOf(Offset.Zero) }
            var scaleX by remember { mutableStateOf(90f) }
            var scaleY by remember { mutableStateOf(20f) }
            var eventOffset by remember { mutableStateOf(Offset.Zero) }
            val radius by remember { mutableStateOf(50f) }
            val listPoints = remember { mutableStateListOf<Offset>() }
            val distance by remember { mutableStateOf(radius / 2) }
            var action by remember { mutableStateOf(-1) }
            var startMove by remember { mutableStateOf(false) }
            val figure by remember { mutableStateOf(Figures.SINUS) }
            var strike by remember { mutableStateOf(false) }
            with(mainScreenHandler) {

                InitListPoints(listPoints, scaleX, scaleY, figure, onAddFloat = {
                    offset = it
                }) {}

                BoxWithCanvas(
                    listPoints, scaleX, scaleY, figure, offset, radius, onEvent =
                    { mAction, off -> action = mAction; eventOffset = off },
                    onPress = { strike = true }
                )

                CheckForStrike(
                    listPoints, radius, eventOffset, offset, scaleX, scaleY, distance, figure,
                    strike, action, onCheck = { offset = it }, onAction = { strike = it })


                AutoMoveBall(listPoints, startMove, onTick = { offset = it })
                { startMove = false }

                Debug(
                    offset, scaleX, scaleY,
                    onClick = { startMove = it },
                    onScaleX = { scaleX = it },
                    onScaleY = { scaleY = it }
                )
            }
        }
    }
}
