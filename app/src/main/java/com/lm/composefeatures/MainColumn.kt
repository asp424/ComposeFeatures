package com.lm.composefeatures

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainColumn(
    isLongPressed: Boolean,
    onActionUp: (Boolean) -> Unit,
    item: @Composable LazyItemScope.(Int, Int, Float, Dp, Float) -> Unit
) {
    val itemSize = remember { 40.dp }
    val countItems = remember { 40 }
    val paddingTop = remember { 105.dp }

    LocalDensity.current.apply {
        var offsetY by remember { mutableStateOf(-1f) }
        rememberLazyListState().apply {
            LazyColumn(
                Modifier
                    .padding(40.dp, paddingTop)
                    .motionEventSpy {
                        if (isLongPressed) offsetY = it.y - paddingTop.toPx()
                        if (it.action == 1) onActionUp(false)
                    }
                    .size(200.dp, 490.dp), this) {

                items(countItems) {
                    item(firstVisibleItemIndex, it, offsetY, itemSize, (itemSize + 5.dp).toPx())
                }
            }
        }
    }
}