package com.lm.composefeatures

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Item(
    startAnim: Boolean,
    i: Int,
    offsetY: Float,
    onLongPress: (Boolean) -> Unit,
    omMotionEvent: (Boolean) -> Unit
) {
    val size = 60.dp
    val density = LocalDensity.current.density
    Box(Modifier
        .width(animateDpAsState(if (startAnim) 120.dp else size).value)
        .height(size)
        .offset(
            animateDpAsState(if (startAnim) size else 0.dp, tween(500)).value,
            ((i * 150f).dp + offsetY.dp) / density
        )
        .padding(10.dp)
        .background(Color.LightGray, RoundedCornerShape(10.dp))
        .pointerInput(Unit) { detectTapGestures(onLongPress = { onLongPress(true) }) }
        .motionEventSpy { if (it.action == 1) omMotionEvent(false) }
    ) {
        Text(
            if (startAnim) i.liter else "$i",
            modifier = Modifier.padding(
                start = animateDpAsState(
                    if (startAnim) 30.dp else 16.dp, tween(500)
                ).value, top = 12.dp
            ), maxLines = 1
        )
    }
}

private val Int.liter
    get() = when (this) {
        0 -> "ноль"
        1 -> "один"
        2 -> "два"
        3 -> "три"
        4 -> "четыре"
        5 -> "пять"
        6 -> "шесть"
        7 -> "семь"
        8 -> "восемь"
        9 -> "девять"
        10 -> "десять"
        else -> "хуй"
    }