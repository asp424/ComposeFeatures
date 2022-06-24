package com.lm.composefeatures.line.ui.main_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.unit.dp
import com.lm.composefeatures.di.compose.ComposeDependencies
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.sin

interface MainScreenHandler {

    fun Float.sinus(width: Float, height: Float, sinScaleX: Float, sinScaleY: Float): Offset

    fun Float.offsetRect(
        width: Float, height: Float, rectSize: Float,
        sinScaleX: Float, sinScaleY: Float
    ): Offset

    @Composable
    fun InitListPoints(
        listPoints: SnapshotStateList<Rect>,
        ball: Float,
        rectSize: Float,
        onSetBall: (Float) -> Unit, onInit: () -> Unit
    )

    @Composable
    fun DrawCanvas(
        listPoints: SnapshotStateList<Rect>,
        radius: Float,
        ball: Float,
        rectSize: Float,
        drawRects: Boolean, sinScaleX: Float,
        sinScaleY: Float
    )

    @Composable
    fun CheckForStrike(
        action: Int,
        radius: Float,
        x: Float,
        y: Float,
        ball: Float, sinScaleX: Float,
        sinScaleY: Float,
        onCheck: (Boolean) -> Unit
    )

    @Composable
    fun MoveBall(
        listPoints: SnapshotStateList<Rect>,
        x: Float,
        y: Float,
        ball: Float,
        strike: Boolean,
        sinScaleX: Float,
        sinScaleY: Float,
        onCheck: (Float) -> Unit
    )

    @Composable
    fun BoxWithCanvas(
        listPoints: SnapshotStateList<Rect>,
        radius: Float,
        ball: Float,
        drawRects: Boolean,
        sinScaleX: Float,
        sinScaleY: Float,
        onEvent: (Int, Float, Float) -> Unit
    )

    @Composable
    fun AutoMoveBall(
        listPoints: SnapshotStateList<Rect>, start: Boolean, onTick: (Float) -> Unit,
        onEnd: () -> Unit
    )

    class Base @Inject constructor(
        private val composeDependencies: ComposeDependencies,
        private val autoMoveBall: MoveBall
    ) : MainScreenHandler {

        override fun Float.sinus(width: Float, height: Float, sinScaleX: Float, sinScaleY: Float) =
            Offset(xSin(width, sinScaleX), ySin(height, sinScaleY))

        override fun Float.offsetRect(
            width: Float, height: Float, rectSize: Float,
            sinScaleX: Float, sinScaleY: Float
        ) =
            Offset(
                xSin(width, sinScaleX) - rectSize / 2,
                ySin(height, sinScaleY) - rectSize / 2
            )

        @Composable
        override fun InitListPoints(
            listPoints: SnapshotStateList<Rect>, ball: Float,
            rectSize: Float, onSetBall: (Float) -> Unit, onInit: () -> Unit
        ) {
            listPoints.apply {
                LaunchedEffect(true) {
                    (0..3000).onEach {
                        (it * 0.01f).also { k ->
                            onSetBall(k)
                            add(
                                Rect(
                                    Offset(k - rectSize, k - rectSize),
                                    Offset(k + rectSize, k + rectSize)
                                )
                            )
                            if (it == 3000) onInit()
                        }
                    }
                }
            }
        }

        @Composable
        override fun CheckForStrike(
            action: Int,
            radius: Float, x: Float, y: Float, ball: Float, sinScaleX: Float,
            sinScaleY: Float, onCheck: (Boolean) -> Unit
        ) = with(composeDependencies.mainScreenDeps()) {
            LaunchedEffect(x, y) {
                withContext(IO) {
                    if (
                        x in ball.xSin(width, sinScaleX) - radius..ball.xSin(
                            width,
                            sinScaleX
                        ) + radius &&
                        y in ball.ySin(height, sinScaleY) - radius..ball.ySin(
                            height,
                            sinScaleY
                        ) + radius &&
                        action == 0
                    ) onCheck(true)
                    if (action == 1) onCheck(false)
                }
            }
        }

        @Composable
        override fun DrawCanvas(
            listPoints: SnapshotStateList<Rect>,
            radius: Float, ball: Float, rectSize: Float, drawRects: Boolean,
            sinScaleX: Float,
            sinScaleY: Float
        ) = with(composeDependencies.mainScreenDeps()) {
            Canvas(Modifier) {
                listPoints.forEach {
                    if (drawRects) {
                        drawRect(
                            topLeft =
                            it.center.x.offsetRect(
                                width, height, rectSize, sinScaleX, sinScaleY
                            ),
                            color = Color.Gray, size = Size(rectSize, rectSize)
                        )
                    }
                    drawCircle(
                        radius = 10f,
                        center = it.center.x.sinus(
                            width, height, sinScaleX, sinScaleY
                        ),
                        color = Color.Black
                    )
                    drawCircle(
                        radius = radius,
                        center = ball.sinus(width, height, sinScaleX, sinScaleY),
                        color = Color.Black
                    )
                }
            }
            Canvas(Modifier) {
                listPoints.forEach {

                    drawCircle(
                        radius = 10f,
                        center = it.center.x.sinus(
                            width, height, sinScaleX, sinScaleY
                        ),
                        color = Color.Black
                    )
                }
            }
        }

        @Composable
        override fun MoveBall(
            listPoints: SnapshotStateList<Rect>,
            x: Float,
            y: Float,
            ball: Float,
            strike: Boolean, sinScaleX: Float,
            sinScaleY: Float,
            onCheck: (Float) -> Unit
        ) {
            with(composeDependencies.mainScreenDeps()) {
                LaunchedEffect(x, y) {
                    withContext(IO) {
                        if (strike) {
                            ((x - width) / sinScaleX).apply {
                                if (this in
                                    listPoints.first().center.x..listPoints.last().center.x
                                ) {
                                    onCheck(this)
                                }
                            }
                        }
                    }
                }
            }
        }

        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        override fun BoxWithCanvas(
            listPoints: SnapshotStateList<Rect>,
            radius: Float,
            ball: Float,
            drawRects: Boolean,
            sinScaleX: Float,
            sinScaleY: Float,
            onEvent: (Int, Float, Float) -> Unit
        ) = Box(modifier = Modifier
            .fillMaxSize()
            .motionEventSpy {
                onEvent(it.action, it.x - radius / 2, it.y - radius / 2)
            }) {
            DrawCanvas(
                listPoints, radius, ball, radius * 3, drawRects, sinScaleX,
                sinScaleY
            )
        }

        @Composable
        override fun AutoMoveBall(
            listPoints: SnapshotStateList<Rect>,
            start: Boolean,
            onTick: (Float) -> Unit,
            onEnd: () -> Unit
        ) = autoMoveBall.AutoMoveBallByTimer(listPoints, start, onTick = { onTick(it) })
        { onEnd() }

        private fun Float.xSin(width: Float, sinScaleX: Float) = (this * sinScaleX) + width

        private fun Float.ySin(height: Float, sinScaleY: Float) = (sinScaleY * sin(this) + height)
    }
}
