package com.lm.composefeatures.ui.custom_slider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import com.lm.composefeatures.di.compose.ComposeDependencies
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MoveBall {

    @Composable
    fun AutoMoveBallByTimer()

    class Base @Inject constructor(
        private val composeDependencies: ComposeDependencies
        ) : MoveBall {

        @Composable
        override fun AutoMoveBallByTimer() = composeDependencies.MainScreenDeps {
            LaunchedEffect(startMove) {
                if (startMove) {
                    withContext(IO) { listPoints.timer(onTick = { it.setOffset })
                    { false.setStartMove } }
                }
            }
        }

        private suspend fun SnapshotStateList<Offset>.timer(
            onTick: (Offset) -> Unit,
            onEnd: () -> Unit
        ) = (0 until size)
            .asFlow()
            .onEach { if (it != 0) delay(1L) }.collect {
                onTick(get(it))
                if (it == size - 1) onEnd()
            }
    }
}