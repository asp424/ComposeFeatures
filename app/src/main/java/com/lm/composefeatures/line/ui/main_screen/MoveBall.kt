package com.lm.composefeatures.line.ui.main_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MoveBall {

    @Composable
    fun AutoMoveBallByTimer(
        listPoints: SnapshotStateList<Rect>, start: Boolean,
        onTick: (Float) -> Unit,
        onEnd: () -> Unit
    )

    class Base @Inject constructor() : MoveBall {

        @Composable
        override fun AutoMoveBallByTimer(
            listPoints: SnapshotStateList<Rect>,
            start: Boolean,
            onTick: (Float) -> Unit,
            onEnd: () -> Unit
        ) {
            LaunchedEffect(start) {
                if (start) {
                    withContext(IO) { listPoints.timer(onTick, onEnd) }
                }
            }
        }

        private suspend fun SnapshotStateList<Rect>.timer(
            onTick: (Float) -> Unit,
            onEnd: () -> Unit
        ) =
            (0 until size).asFlow().onEach { if (it != 0) delay(1L) }.collect {
                onTick(get(it).center.x)
                if (it == size - 1) onEnd()
            }
    }
}