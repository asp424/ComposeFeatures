package com.lm.composefeatures.line.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.lm.composefeatures.line.ui.main_screen.Figures
import com.lm.composefeatures.line.ui.main_screen.MainScreenHandler
import javax.inject.Inject

interface Screens {

    @Composable
    fun MainScreen()

    class Base @Inject constructor(
        private val mainScreenHandler: MainScreenHandler,
        private val viewModels: ViewModels
    ) : Screens {

        @Composable
        override fun MainScreen() {
            var offset by remember { mutableStateOf(Offset.Zero) }
            var sinScaleX by remember { mutableStateOf(90f) }
            var sinScaleY by remember { mutableStateOf(20f) }
            var eventOffset by remember { mutableStateOf(Offset.Zero) }
            val radius by remember { mutableStateOf(50f) }
            val listPoints = remember { mutableStateListOf<Offset>() }
            val distance by remember { mutableStateOf(radius / 2) }
            var action by remember{ mutableStateOf(-1) }
            var startMove by remember { mutableStateOf(false) }
            var buttonText by remember { mutableStateOf("Go") }
            val figure by remember { mutableStateOf(Figures.SINUS) }
            var strike by remember { mutableStateOf(false) }
            with(mainScreenHandler) {
                InitListPoints(listPoints, sinScaleX, sinScaleY, figure, onAddFloat = {
                    offset = it
                }) {}

                BoxWithCanvas(
                    listPoints, sinScaleX, sinScaleY, figure, offset, radius, onEvent =
                    { mAction, off ->
                        action = mAction
                        eventOffset = off
                    }, onPress = {
                        strike = true
                    }, distance = distance
                )

                CheckForStrike(
                    listPoints, radius, eventOffset, offset, sinScaleX, sinScaleY, distance, figure,
                    strike, action, onCheck = { offset = it }, onAction = { strike = it })


                AutoMoveBall(listPoints, startMove, onTick = { offset = it })
                { startMove = false; buttonText = "Go" }

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 200.dp, start = 40.dp, end = 40.dp),
                    verticalArrangement = Center,
                    horizontalAlignment = CenterHorizontally
                ) {

                    Slider(
                        value = sinScaleY,
                        onValueChange = {
                            sinScaleY = it
                        },
                        valueRange = (0f..100f),
                        modifier = Modifier
                    )
                    Slider(
                        value = sinScaleX, onValueChange = {
                            sinScaleX = it
                        }, valueRange = (0f..90f), modifier = Modifier
                    )

                    Button(onClick = {
                        startMove = true
                        buttonText = "Stop"
                    }) {
                        Text(text = buttonText)
                    }
                }
                Column(Modifier.fillMaxSize().padding(top = 30.dp),
                    horizontalAlignment = CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Icon(
                        Icons.Default.Abc, null,
                        modifier = Modifier.size(offset.x.dp / 5))
                }
            }
        }
    }
}
