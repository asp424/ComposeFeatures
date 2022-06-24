package com.lm.composefeatures.line.ui.main_screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.lm.composefeatures.di.compose.ComposeDependencies
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.sin

interface MainScreenHandler {

    fun Float.sinus(width: Float, sinScaleX: Float, height: Float, sinScaleY: Float): Offset

    @Composable
    fun InitListPoints(
        listPoints: SnapshotStateList<Float>,
        onAddFloat: (Float) -> Unit, onInit: () -> Unit
    )

    @Composable
    fun DrawCanvas(
        listPoints: SnapshotStateList<Float>,
        radius: Float,
        ballX: Float,
        sinScaleX: Float,
        sinScaleY: Float,
        width: Float,
        height: Float
    )

    @Composable
    fun CheckForStrike(
        action: Int, listPoints: SnapshotStateList<Float>,
        radius: Float, eventX: Float, eventY: Float, ballX: Float, sinScaleX: Float,
        sinScaleY: Float, width: Float, height: Float, distance: Float, onCheck: (Float) -> Unit
    )

    @Composable
    fun BoxWithCanvas(
        listPoints: SnapshotStateList<Float>,
        radius: Float,
        ball: Float,
        sinScaleX: Float,
        sinScaleY: Float,
        width: Float,
        height: Float,
        onEvent: (Int, Float, Float) -> Unit
    )

    @Composable
    fun AutoMoveBall(
        listPoints: SnapshotStateList<Float>, start: Boolean, onTick: (Float) -> Unit,
        onEnd: () -> Unit
    )

    class Base @Inject constructor(
        private val composeDependencies: ComposeDependencies,
        private val autoMoveBall: MoveBall
    ) : MainScreenHandler {

        @Composable
        override fun CheckForStrike(
            action: Int, listPoints: SnapshotStateList<Float>,
            radius: Float, eventX: Float, eventY: Float, ballX: Float, sinScaleX: Float,
            sinScaleY: Float, width: Float, height: Float, distance: Float, onCheck: (Float) -> Unit
        ) = LaunchedEffect(eventX) {
            withContext(IO) {
                if (
                    check(ballX, width, eventX, eventY,
                        radius + distance, height, action, sinScaleX, sinScaleY)
                ) {
                    ((eventX - width) / sinScaleX).apply {
                        if (this in listPoints.first()..listPoints.last()
                        ) onCheck(this)

                    }
                }
            }
        }

        private fun check(
            ballX: Float, width: Float, eventX: Float, eventY: Float, radius: Float,
            height: Float, action: Int, sinScaleX: Float, sinScaleY: Float
        ) = if (action == 0 || action == 2) eventX in ballX.xSin(width, sinScaleX) - radius..ballX.xSin(
            width,
            sinScaleX
        ) + radius &&
                eventY in ballX.ySin(height, sinScaleY) - radius..ballX.ySin(height, sinScaleY) + radius
        else false

        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        override fun BoxWithCanvas(
            listPoints: SnapshotStateList<Float>,
            radius: Float,
            ball: Float,
            sinScaleX: Float,
            sinScaleY: Float,
            width: Float,
            height: Float,
            onEvent: (Int, Float, Float) -> Unit
        ) = Box(modifier = Modifier
            .fillMaxSize()
            .motionEventSpy {
                onEvent(it.action, it.x - radius / 2, it.y - radius / 2)
                Log.d(
                    "My",
                    listPoints
                        .indexOf(it.x)
                        .toString()
                )

            }) {
            DrawCanvas(
                listPoints, radius, ball, sinScaleX, sinScaleY, width, height
            )
        }

        @Composable
        override fun DrawCanvas(
            listPoints: SnapshotStateList<Float>,
            radius: Float,
            ballX: Float,
            sinScaleX: Float,
            sinScaleY: Float,
            width: Float,
            height: Float
        ) =
            Canvas(Modifier) {
                listPoints.forEach {
                    drawCircle(
                        radius = 10f,
                        center = it.sinus(width, sinScaleX, height, sinScaleY),
                        color = Color.Black
                    )
                }
                drawCircle(
                    radius = radius,
                    center = ballX.sinus(width, sinScaleX, height, sinScaleY),
                    color = Color.Black
                )
            }

        @Composable
        override fun InitListPoints(
            listPoints: SnapshotStateList<Float>,
            onAddFloat: (Float) -> Unit, onInit: () -> Unit
        ) {
            listPoints.apply {
                LaunchedEffect(true) {
                    (0..3000).onEach {
                        (it * 0.01f).also { k ->
                            onAddFloat(k)
                            add(k)
                            if (it == 3000) onInit()
                        }
                    }
                }
            }
        }

        override fun Float.sinus(width: Float, sinScaleX: Float, height: Float, sinScaleY: Float) =
            Offset(xSin(width, sinScaleX), ySin(height, sinScaleY))

        private fun Float.xSin(width: Float, sinScaleX: Float) = this * sinScaleX + width

        private fun Float.ySin(height: Float, sinScaleY: Float) = sinScaleY * sin(this) + height

        @Composable
        override fun AutoMoveBall(
            listPoints: SnapshotStateList<Float>,
            start: Boolean,
            onTick: (Float) -> Unit,
            onEnd: () -> Unit
        ) = autoMoveBall.AutoMoveBallByTimer(listPoints, start, onTick = { onTick(it) })
        { onEnd() }
    }
}
