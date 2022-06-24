package com.lm.composefeatures.line.ui

import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.lm.composefeatures.di.compose.ComposeDependencies
import com.lm.composefeatures.line.ui.main_screen.MainScreenHandler
import javax.inject.Inject

interface Screens {

    @Composable
    fun MainScreen()

    class Base @Inject constructor(
        private val mainScreenHandler: MainScreenHandler,
        private val viewModels: ViewModels,
        private val composeDependencies: ComposeDependencies
    ) : Screens {

        @Composable
        override fun MainScreen() {
            var ballX by remember { mutableStateOf(0f) }
            var sinScaleX by remember { mutableStateOf(30f) }
            var sinScaleY by remember { mutableStateOf(200f) }
            var eventX by remember { mutableStateOf(0f) }
            var eventY by remember { mutableStateOf(0f) }
            val radius by remember { mutableStateOf(50f) }
            val listPoints = remember { mutableStateListOf<Float>() }
            val distance by remember { mutableStateOf(10f) }
            var action by remember { mutableStateOf(-1) }
            var startMove by remember { mutableStateOf(false) }
            var buttonText by remember { mutableStateOf("Go") }
            composeDependencies.mainScreenDeps().apply {
                with(mainScreenHandler) {

                    InitListPoints(
                        listPoints,
                        onAddFloat = { ballX = it }) {}

                    BoxWithCanvas(
                        listPoints,
                        radius,
                        ballX,
                        sinScaleX,
                        sinScaleY,
                        width,
                        height,
                        onEvent = { mAction, mX, mY ->
                            action = mAction; eventX = mX; eventY = mY
                        }
                    )

                    CheckForStrike(
                        action,
                        listPoints,
                        radius,
                        eventX,
                        eventY,
                        ballX,
                        sinScaleX,
                        sinScaleY,
                        width,
                        height,
                        distance
                    ) { ballX = it }

                    AutoMoveBall(listPoints, startMove, onTick = { ballX = it })
                    { startMove = false; buttonText = "Go" }

                    Column(
                        Modifier
                            .fillMaxSize().padding(top = 100.dp)
                            , verticalArrangement = Center,
                        horizontalAlignment = CenterHorizontally
                    ) {

                            Slider(
                                value = sinScaleY,
                                onValueChange = {
                                    sinScaleY = it
                                },
                                valueRange = (0f..300f),
                                modifier = Modifier
                            )
                            Slider(
                                value = sinScaleX, onValueChange = {
                                    sinScaleX = it
                                }, valueRange = (0f..50f), modifier = Modifier

                            )

                        Button(onClick = {
                            startMove = true
                            buttonText = "Stop"
                        }) {
                            Text(text = buttonText)
                        }
                    }
                    Column(Modifier.fillMaxSize().padding(top = 30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Icon(
                            Icons.Default.Abc, null,
                            modifier = Modifier.size(ballX.dp * 10))
                    }
                }
            }
        }
    }
}
