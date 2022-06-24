package com.lm.composefeatures

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import kotlin.math.sin


@Composable
fun CustomSlider() {
    var ballX by remember { mutableStateOf(0f) }
    val sinScaleX by remember { mutableStateOf(30f) }
    val sinScaleY by remember { mutableStateOf(50f) }
    var eventX by remember { mutableStateOf(0f) }
    var eventY by remember { mutableStateOf(0f) }
    val radius by remember { mutableStateOf(50f) }
    val listPoints = remember { mutableStateListOf<Float>() }
    val distance by remember { mutableStateOf(10f) }
    var action by remember { mutableStateOf(-1) }
    with(LocalConfiguration.current) {
        with(LocalDensity.current) {
            val width = screenWidthDp.dp.toPx() / 10
            val height = screenHeightDp.dp.toPx() / 3

            InitListPoints(listPoints, onAddFloat = { ballX = it }) {}

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

            Column(Modifier.fillMaxSize().padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
            ) {
                Icon(Icons.Default.Abc, null,
                    modifier = Modifier.size(ballX.dp * 10))
            }
        }
    }
}

@Composable
private fun CheckForStrike(
    action: Int, listPoints: SnapshotStateList<Float>,
    radius: Float, eventX: Float, eventY: Float, ballX: Float, sinScaleX: Float,
    sinScaleY: Float, width: Float, height: Float, distance: Float, onCheck: (Float) -> Unit
) = LaunchedEffect(eventX) {
    withContext(IO) {
        if (
            check(ballX, width, eventX, eventY, radius + distance, height, action, sinScaleX, sinScaleY)
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
private fun BoxWithCanvas(
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
    }) {
    DrawCanvas(
        listPoints, radius, ball, sinScaleX, sinScaleY, width, height
    )
}

@Composable
private fun DrawCanvas(
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
                color = Black
            )
        }
            drawCircle(
                radius = radius,
                center = ballX.sinus(width, sinScaleX, height, sinScaleY),
                color = Black
            )
        }

@Composable
private fun InitListPoints(
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

private fun Float.sinus(width: Float, sinScaleX: Float, height: Float, sinScaleY: Float) =
    Offset(xSin(width, sinScaleX), ySin(height, sinScaleY))

private fun Float.xSin(width: Float, sinScaleX: Float) = this * sinScaleX + width

private fun Float.ySin(height: Float, sinScaleY: Float) = sinScaleY * sin(this) + height
