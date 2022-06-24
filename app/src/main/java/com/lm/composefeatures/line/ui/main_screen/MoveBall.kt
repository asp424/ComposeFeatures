package com.lm.composefeatures.line.ui.main_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
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
        listPoints: SnapshotStateList<Offset>, start: Boolean,
        onTick: (Offset) -> Unit,
        onEnd: () -> Unit
    )

    class Base @Inject constructor() : MoveBall {

        @Composable
        override fun AutoMoveBallByTimer(
            listPoints: SnapshotStateList<Offset>,
            start: Boolean,
            onTick: (Offset) -> Unit,
            onEnd: () -> Unit
        ) {
            LaunchedEffect(start) {
                if (start) {
                    withContext(IO) { listPoints.timer(onTick, onEnd) }
                }
            }
        }

        private suspend fun SnapshotStateList<Offset>.timer(
            onTick: (Offset) -> Unit,
            onEnd: () -> Unit
        ) =
            (0 until size).asFlow().onEach { if (it != 0) delay(1L) }.collect {
                onTick(get(it))
                if (it == size - 1) onEnd()
            }
    }
}