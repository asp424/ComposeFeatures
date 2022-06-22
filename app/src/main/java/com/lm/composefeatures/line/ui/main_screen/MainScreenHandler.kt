package com.lm.composefeatures.line.ui.main_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import com.lm.composefeatures.di.compose.ComposeDependencies
import javax.inject.Inject
import kotlin.math.sin

interface MainScreenHandler {
    fun Float.sinus(width: Float, height: Float, sinScaleX: Int, sinScaleY: Int): Offset
    fun Float.offsetRect(
        width: Float, height: Float, rectSize: Float,
        sinScaleX: Int, sinScaleY: Int
    ): Offset

    @Composable
    fun InitListPoints(
        listPoints: SnapshotStateList<Rect>,
        ball: Float,
        rectSize: Float,
        onSetBall: (Float) -> Unit
    )

    @Composable
    fun DrawCanvas(
        listPoints: SnapshotStateList<Rect>,
        radius: Float,
        ball: Float,
        rectSize: Float,
        drawRects: Boolean
    )

    @Composable
    fun MotionEventHandler(
        action: Int,
        radius: Float,
        x: Float,
        y: Float,
        ball: Float,
        onCheck: (Boolean) -> Unit
    )

    @Composable
    fun MoveBall(
        listPoints: SnapshotStateList<Rect>,
        x: Float,
        y: Float,
        ball: Float,
        strike: Boolean,
        onCheck: (Float) -> Unit
    )

    @Composable
    fun BoxWithCanvas(
        listPoints: SnapshotStateList<Rect>,
        radius: Float,
        ball: Float,
        drawRects: Boolean,
        onEvent: (Int, Float, Float) -> Unit
    )

    class Base @Inject constructor(
        private val composeDependencies: ComposeDependencies
    ) : MainScreenHandler {

        override fun Float.sinus(width: Float, height: Float, sinScaleX: Int, sinScaleY: Int) =
            Offset(xSin(width, sinScaleX), ySin(height, sinScaleY))

        override fun Float.offsetRect(
            width: Float, height: Float, rectSize: Float,
            sinScaleX: Int, sinScaleY: Int
        ) =
            Offset(
                xSin(width, sinScaleX) - rectSize / 2,
                ySin(height, sinScaleY) - rectSize / 2
            )

        @Composable
        override fun InitListPoints(
            listPoints: SnapshotStateList<Rect>, ball: Float,
            rectSize: Float, onSetBall: (Float) -> Unit
        ) {
            listPoints.apply {
                LaunchedEffect(true) {
                    (0..3000).onEach {
                        (it * 0.01f).also { k ->
                            onSetBall(k)
                            add(
                                Rect(
                                    Offset(
                                        k - rectSize,
                                        k - rectSize
                                    ),
                                    Offset(
                                        k + rectSize,
                                        k + rectSize
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }

            @Composable
            override fun MotionEventHandler(
                action: Int,
                radius: Float, x: Float, y: Float, ball: Float, onCheck: (Boolean) -> Unit
            ) = with(composeDependencies.mainScreenDeps()) {
                LaunchedEffect(x, y) {
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

            @Composable
            override fun DrawCanvas(
                listPoints: SnapshotStateList<Rect>,
                radius: Float, ball: Float, rectSize: Float, drawRects: Boolean
            ) =
                with(composeDependencies.mainScreenDeps()) {
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
                        }
                        drawCircle(
                            radius = radius,
                            center = ball.sinus(width, height, sinScaleX, sinScaleY),
                            color = Color.Black
                        )
                    }
                }

            @Composable
            override fun MoveBall(
                listPoints: SnapshotStateList<Rect>,
                x: Float,
                y: Float,
                ball: Float,
                strike: Boolean,
                onCheck: (Float) -> Unit
            ) {
                with(composeDependencies.mainScreenDeps()) {
                    LaunchedEffect(x, y) {
                        if (strike) {
                            ((x - width) / sinScaleX).apply {
                                if (this in listPoints.first().center.x..listPoints.last().center.x) {
                                    onCheck(this)
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
                radius: Float, ball: Float, drawRects: Boolean, onEvent: (Int, Float, Float) -> Unit
            ) = Box(modifier = Modifier
                .fillMaxSize()
                .motionEventSpy {
                    onEvent(it.action, it.x - radius / 2, it.y - radius / 2)
                }) { DrawCanvas(listPoints, radius, ball, radius * 3, drawRects) }

            private fun Float.xSin(width: Float, sinScaleX: Int) = (this * sinScaleX) + width

            private fun Float.ySin(height: Float, sinScaleY: Int) = (sinScaleY * sin(this) + height)
        }
    }
