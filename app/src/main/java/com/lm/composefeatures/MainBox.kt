package com.lm.composefeatures

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainBox(
    isLongPressed: Boolean,
    onMotionEvent: (Float) -> Unit,
    onLongPress: (Boolean) -> Unit,
    content: @Composable (BoxScope) -> Unit
) {
    Box(Modifier
            .padding(start = 40.dp, top = 60.dp)
            .background(Color.White, RoundedCornerShape(10.dp))
            .shadow(2.dp)
            .motionEventSpy {
                //if (isLongPressed)
                    onMotionEvent(it.y - 255)
                if (it.action == 1) onLongPress(false)
            }
            .size(200.dp, 510.dp)
    ) { content(this) }
}