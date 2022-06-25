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
import kotlin.math.sin

interface MainScreenHandler {

    @Composable
    fun InitListPoints(figure: Figures)

    @Composable
    fun DrawFigure(radius: Float)

    @Composable
    fun DrawBall(radius: Float)

    @Composable
    fun CheckForStrike(radius: Float, distance: Float, figure: Figures)

    @Composable
    fun BoxWithCanvas(figure: Figures, radius: Float)

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
        override fun CheckForStrike(radius: Float, distance: Float, figure: Figures) =
            with(composeDependencies.mainScreenDepsLocal()) {
                with(handlerUtils) {
                    if (strike) CheckInListAndGetSinusByEventX(figure)
                    if (!CompareOffsets(radius + distance) || action == 1)
                        false.setStrike
                }
            }

        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        override fun BoxWithCanvas(figure: Figures, radius: Float) =
            composeDependencies.MainScreenDeps {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .motionEventSpy { it.action.setAction; Offset(it.x, it.y).setEventOffset }
                ) {
                    DrawFigure(radius)
                }
            }

        @Composable
        override fun DrawFigure(radius: Float) =
            composeDependencies.MainScreenDeps {
                Canvas(Modifier) {
                    listPoints.forEach {
                        handlerUtils.apply { draw(10f, it) }
                    }
                }
                DrawBall(radius)
            }

        @Composable
        override fun DrawBall(radius: Float) = with(handlerUtils) {
            composeDependencies.MainScreenDeps {
                Box(
                    Modifier
                        .boxMod(radius)
                        .pointerInput(Unit) {
                            detectTapGestures(onPress = { true.setStrike })
                        }
                ) { Canvas(Modifier) { draw(radius, Offset(radius, radius)) } }
            }
        }

        @Composable
        override fun InitListPoints(figure: Figures) {
            with(composeDependencies.mainScreenDepsLocal()) {
                LaunchedEffect(scaleX, scaleY) {
                    listPoints.clear()
                    (0..1000).onEach {
                        (it * 0.01f).apply {
                            listPoints.add(
                                Offset(this * scaleX + width, scaleY * sin(this) + height)
                                    .apply { setOffset }
                            )
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
