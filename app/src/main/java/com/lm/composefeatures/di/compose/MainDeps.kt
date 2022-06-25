package com.lm.composefeatures.di.compose

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset

data class MainDeps(
    val width: Float,
    val height: Float,
    private val _offset: MutableState<Offset>,
    private val _scaleX: MutableState<Float>,
    private val _scaleY: MutableState<Float>,
    private val _eventOffset: MutableState<Offset>,
    val listPoints: SnapshotStateList<Offset>,
    private val _action: MutableState<Int>,
    private val _startMove: MutableState<Boolean>,
    private val _strike: MutableState<Boolean>
) {
    val Offset.setOffset get() = run { _offset.value = this }
    val Float.setScaleX get() = run { _scaleX.value = this }
    val Float.setScaleY get() = run { _scaleY.value = this }
    val Offset.setEventOffset get() = run { _eventOffset.value = this }
    val Int.setAction get() = run { _action.value = this }
    val Boolean.setStartMove get() = run { _startMove.value = this }
    val Boolean.setStrike get() = run { _strike.value = this }

    val offset get() = _offset.value
    val scaleX get() = _scaleX.value
    val scaleY get() = _scaleY.value
    val eventOffset get() = _eventOffset.value
    val action get() = _action.value
    val startMove get() = _startMove.value
    val strike get() = _strike.value
}
