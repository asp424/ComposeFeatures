package com.lm.composefeatures.custom_slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import com.lm.composefeatures.di.compose.ComposeDependencies
import javax.inject.Inject

interface MainScreenHandler {

    @Composable
    fun InitListPoints()

    @Composable
    fun DrawFigure()

    @Composable
    fun DrawBall()

    @Composable
    fun CheckForStrike()

    @Composable
    fun BoxWithCanvas()

    @Composable
    fun AutoMoveBall()

    @Composable
    fun Debug()

    class Base @Inject constructor(
        private val composeDependencies: ComposeDependencies,
        private val autoMoveBall: MoveBall,
        private val debugWidgets: DebugWidgets,
        private val handlerUtils: HandlerUtils
    ) : MainScreenHandler {

        @Composable
        override fun CheckForStrike() =
            with(composeDependencies.mainScreenDepsLocal()) {
                with(handlerUtils) {
                    if (strike) CheckInListAndGetSinusByEventX()
                    if (!CompareOffsets() || action == 1)
                        false.setStrike
                }
            }

        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        override fun BoxWithCanvas() =
            composeDependencies.MainScreenDeps {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .motionEventSpy { it.action.setAction; Offset(it.x, it.y).setEventOffset }
                ) {
                    DrawFigure()
                }
            }

        @Composable
        override fun DrawFigure() =
            composeDependencies.MainScreenDeps {
                Canvas(Modifier) {
                    listPoints.forEach { handlerUtils.apply { draw(10f, it) } }
                }
                DrawBall()
            }

        @Composable
        override fun DrawBall() = with(handlerUtils) {
            composeDependencies.MainScreenDeps {
                Box(
                    Modifier
                        .boxMod()
                        .pointerInput(Unit) {
                            detectTapGestures(onPress = { true.setStrike })
                        }
                ) { Canvas(Modifier) { draw(radius, Offset(radius, radius)) } }
            }
        }

        @Composable
        override fun InitListPoints() {
            with(composeDependencies.mainScreenDepsLocal()) {
                LaunchedEffect(scaleX, scaleY) {
                    with(listPoints) {
                        clear()
                        handlerUtils.apply {
                            (0..figureLength).onEach {
                                add(
                                    it.sinusOffset(scaleX, scaleY, width, height)
                                        .apply { setOffset }
                                )
                            }
                        }
                    }
                }
            }
        }

        @Composable
        override fun AutoMoveBall() = autoMoveBall.AutoMoveBallByTimer()

        @Composable
        override fun Debug() = debugWidgets.Debug()
    }
}
