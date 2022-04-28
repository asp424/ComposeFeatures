package com.lm.composefeatures

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovingList() {
    var isLongPressed by remember { mutableStateOf(false) }
    Scaffold {
        CardBack()
        MainColumn(isLongPressed, { isLongPressed = it }) {
                fVIIndex, index, offsetY, size, sizeToPx ->

            var startAnim by remember { mutableStateOf(false) }

            OnActionMoveEvent(offsetY + fVIIndex * sizeToPx, (index * sizeToPx), isLongPressed)
                    { startAnim = it }

            LaunchedEffect(isLongPressed) { if (!isLongPressed) startAnim = false }

            Item(startAnim, size) { isLongPressed = it; startAnim = it }
        }
    }
}

val <T> T.log get() = Log.d("My", toString())

@Composable
private fun OnActionMoveEvent(
    y: Float, i: Float, isLongPressed: Boolean, startAnim: (Boolean) -> Unit
) {
    val d = with(LocalDensity.current){ 40.dp.toPx() }
    (y in i .. i + d).also {
        LaunchedEffect(it) {
            withContext(IO) {
                if (it && isLongPressed) startAnim(true)
                else {
                    delay(100); startAnim(false)
                }
            }
        }
    }
}











