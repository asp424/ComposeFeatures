package com.lm.composefeatures

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Item(
    startAnim: Boolean,
    size: Dp,
    onMotionEvent: (Boolean) -> Unit
) {
    Box(Modifier.padding(start = 10.dp, bottom = 5.dp)) {
        Box(
            Modifier
                .width(animateDpAsState(if (startAnim) 120.dp else size).value)
                .height(size)
                .offset(
                    animateDpAsState(if (startAnim) size else 0.dp, tween(500)).value, 0.dp
                )
                .background(Black, RoundedCornerShape(10.dp))
                .pointerInput(Unit) { detectTapGestures(onLongPress = { onMotionEvent(true) }) },
            contentAlignment = Alignment.Center
        ) { Icon(remember { iconsList.random() }, "star", tint = White) }
    }
}

val iconsList = listOf(
    Icons.Outlined.Cloud,
    Icons.Outlined.Abc,
    Icons.Outlined.Dashboard,
    Icons.Outlined.Wallpaper,
    Icons.Outlined.Face,
    Icons.Outlined.Vaccines,
    Icons.Outlined.VerticalAlignCenter,
    Icons.Outlined.GMobiledata,
    Icons.Outlined.DataObject,
    Icons.Outlined.CalendarMonth,
    Icons.Outlined.ForwardToInbox,
    Icons.Outlined.HdrAuto,
    Icons.Outlined.RampLeft,
    Icons.Outlined.Eco,
    Icons.Outlined.Yard,
    Icons.Outlined.Image,
    Icons.Outlined.Balcony,
    Icons.Outlined.NavigateBefore,
    Icons.Outlined.VapeFree,
    Icons.Outlined.Undo
)

