package com.lm.composefeatures.line.ui

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
            var strike by remember { mutableStateOf(false) }
            var startMove by remember { mutableStateOf(false) }
            var eventX by remember { mutableStateOf(0f) }
            var eventY by remember { mutableStateOf(0f) }
            val radius by remember { mutableStateOf(50f) }
            var buttonText by remember { mutableStateOf("Go") }
            val listPoints = remember { mutableStateListOf<Rect>() }
            var action by remember { mutableStateOf(-1) }
            val owner = checkNotNull(LocalViewModelStoreOwner.current)
            composeDependencies.mainScreenDeps().apply {
                with(mainScreenHandler) {
                    InitListPoints(
                        listPoints, ballX, radius, onSetBall = { ballX = it },
                    ) {}

                    CheckForStrike(action, radius, eventX, eventY, ballX, sinScaleX, sinScaleY) {
                        if (!startMove) strike = it
                    }

                    MoveBall(listPoints, eventX, eventY, ballX, strike, sinScaleX, sinScaleY)
                    { ballX = it }

                    BoxWithCanvas(listPoints, radius, ballX, true, sinScaleX, sinScaleY)
                    { mAction, mX, mY ->
                        action = mAction; eventX = mX; eventY = mY
                    }

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
                }
            }
        }
    }
}
