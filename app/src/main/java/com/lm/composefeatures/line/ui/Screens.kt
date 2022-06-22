package com.lm.composefeatures.line.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.motionEventSpy
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

        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        override fun MainScreen() {
            with(viewModels.mainViewModel()) {
                with(mainScreenHandler) {
                    composeDependencies.mainDeps().apply {
                        var ball by remember { mutableStateOf(0f) }
                        var strike by remember { mutableStateOf(false) }
                        var x by remember { mutableStateOf(0f) }
                        var y by remember { mutableStateOf(0f) }
                        var rectSize by remember { mutableStateOf(150f) }
                        var text by remember { mutableStateOf("uncheck") }
                        val radius by remember { mutableStateOf(50f) }
                        val listPoints = remember {
                            mutableStateListOf<Rect>().apply {
                                (0..500).onEach {
                                    ball += 0.01f
                                    add(
                                        Rect(
                                            Offset(ball - rectSize, ball - rectSize),
                                            Offset(ball + rectSize, ball + rectSize)
                                        )
                                    )
                                }
                            }
                        }
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .motionEventSpy {
                                x = it.x - radius / 2
                                y = it.y - radius / 2
                                if (it.checkForStrike(width, height, ball, radius)) strike =
                                    true
                                if (it.action == 1) strike = false

                            }) {
                            Canvas(listPoints, width, height, rectSize, radius, ball)
                        }

                        LaunchedEffect(x, y) {
                            if (strike) {
                                (((x) - width) / 80).apply {
                                    if (this in listPoints.first().center.x..listPoints.last().center.x) {
                                        ball = this
                                    }
                                }
                            }
                        }
                        Column() {
                            Text(text = ass())
                            Text(text = Offset(x, y).toString())
                            Text(text = ball.offset(width, height).toString())
                            Text(text = listPoints.last().center.toString())
                        }
                    }
                }
            }
        }
    }
}
