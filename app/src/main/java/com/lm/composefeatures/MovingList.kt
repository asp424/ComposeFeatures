package com.lm.composefeatures

import android.util.Log
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@Composable
fun MovingList() {
    var offsetY by remember { mutableStateOf(-100f) }
    var isLongPressed by remember { mutableStateOf(false) }
    MainBox(isLongPressed, { offsetY = it }, { isLongPressed = it }) {
        repeat(20) { i ->

            var startAnim by remember { mutableStateOf(false) }

            OnFingerEvent(offsetY, i, isLongPressed) { startAnim = it }

            OnActionUpEvent(isLongPressed) { startAnim = it }

            Item(startAnim, i, offsetY, { startAnim = it; isLongPressed = it },
                { isLongPressed = it })
        }
    }
}

val <T> T.log get() = Log.d("My", toString())

private fun Float.check(i: Float) = this in i - 40..i + 80

@Composable
private fun OnActionUpEvent(isLongPressed: Boolean, startAnim: (Boolean) -> Unit) =
    LaunchedEffect(isLongPressed) { if (!isLongPressed) startAnim(false) }


@Composable
private fun OnFingerEvent(
    y: Float, i: Int, isLongPressed: Boolean, startAnim: (Boolean) -> Unit
) = with(y.check(i * 150f)) {
        LaunchedEffect(this) {
            if (isLongPressed) if (this@with) startAnim(true)
            else {
                delay(100); startAnim(false)
            }
        }
    }








