package com.lm.composefeatures.di.compose

import android.view.MotionEvent
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import javax.inject.Inject

interface ComposeValues {

    @Composable
    fun mainScreenValues(): MainDeps

    class Base @Inject constructor() : ComposeValues {

        @Composable
        override fun mainScreenValues() =
            with(LocalConfiguration.current) {
                with(LocalDensity.current) {
                    MainDeps(
                        width = screenWidthDp.dp.toPx() / 10,
                        height = screenHeightDp.dp.toPx() / 3,
                        sinScaleX = 30,
                        sinScaleY = 200
                    )
                }
            }
    }
}