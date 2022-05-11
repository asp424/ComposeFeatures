package com.lm.fantasticprogress

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CircleProgress(
    type: ProgressCircleType,
    visible: Boolean,
    minSize: Float = 0f,
    maxSize: Float = 1f
) {
    val map = remember { mutableStateListOf<Boolean>().apply { (-1 until 12).map { add(false) } } }
    var scale by remember { mutableStateOf(false) }
    var tick by remember { mutableStateOf(0) }
    var rotation by remember { mutableStateOf(0f) }
    val workState by rememberUpdatedState(true)
    var jobSize: Job? by remember { mutableStateOf(null) }
    val coroutine = rememberCoroutineScope()
    LaunchedEffect(workState) {
        if (workState) {
            jobSize?.cancel()
            jobSize = coroutine.launch(Dispatchers.IO) {
                oneSideWaveTimer(speed = type.speed, countItems = 11) { t, s ->
                    tick = t
                    scale = s
                    rotation += 1f
                }
            }
        } else jobSize?.cancel()

    }
    LaunchedEffect(tick, scale) { map[tick] = scale }
    Visibility(visible) {
        repeat(12) {
            val float by animateFloatAsState(
                if (map[it]) minSize else maxSize,
                animationSpec = tween(type.speedResize)
            )
            Canvas(modifier = Modifier
                .graphicsLayer {
                    scaleX = float
                    scaleY = float
                    rotationZ = rotation
                }, onDraw = {
                drawCircle(
                    color = Color.Black,
                    center = Offset(
                        60 * cos(30 * it * 0.0174444444).toFloat(),
                        60 * sin(30 * it * 0.0174444444).toFloat()
                    ), radius = 10f
                )
            })
        }
    }

    DisposableEffect(true) {
        onDispose { jobSize?.cancel() }
    }
}

internal suspend fun oneSideWaveTimer(
    speed: Long,
    countItems: Int,
    call: (Int, Boolean) -> Unit
) {
    var tick = 0
    var side = 0

    while (true) {
        if (tick in 0..countItems)
            call(
                tick, when (side) {
                    0 -> true
                    1 -> false
                    2 -> true
                    3 -> false
                    else -> true
                }
            )
        delay(speed)
        when (side) {
            1 -> tick--
            2 -> tick--
        }
        when {
            tick == countItems && side == 0 -> {
                side = 2
            }
            tick == -1 && side == 2 -> {
                side = 3
            }
            tick == countItems && side == 3 -> {

                tick = -1
                side = 0
            }
        }

        when (side) {
            0 -> tick++
            3 -> tick++
        }
    }
}


sealed class ProgressCircleType {
    object Atom : ProgressCircleType()
    object Slow : ProgressCircleType()
    object Middle : ProgressCircleType()
    object Fast : ProgressCircleType()
    object Random : ProgressCircleType()
}

internal val ProgressCircleType.speed get() =
    when(this){
        is ProgressCircleType.Atom -> 3L
        is ProgressCircleType.Slow -> 100L
        is ProgressCircleType.Middle -> 50L
        is ProgressCircleType.Fast -> 30L
        is ProgressCircleType.Random -> 10L
    }

internal val ProgressCircleType.speedResize get() =
    when(this){
        is ProgressCircleType.Atom -> 100
        is ProgressCircleType.Slow -> 500
        is ProgressCircleType.Middle -> 500
        is ProgressCircleType.Fast -> 500
        is ProgressCircleType.Random -> 1000
    }

@ExperimentalAnimationApi
@Composable
internal fun Visibility(visible: Boolean, content: @Composable (AnimatedVisibilityScope.() -> Unit)) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(

            initialOffsetY = { with(density) { 200.dp.roundToPx() } }
        ) + expandVertically(

            expandFrom = Alignment.Bottom
        ) + fadeIn(

            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        content(this)
    }
}
