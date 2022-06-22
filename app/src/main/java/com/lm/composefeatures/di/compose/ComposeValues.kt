package com.lm.composefeatures.di.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import javax.inject.Inject

interface ComposeValues {

    @Composable
    fun baseValues(): MainDeps

    class Base @Inject constructor(): ComposeValues {
        @Composable
        override fun baseValues() =
            with(LocalConfiguration.current) {
                with(LocalDensity.current) {
                    MainDeps(
                        screenWidthDp.dp.toPx() / 3,
                        screenHeightDp.dp.toPx() / 3
                    )
                }
            }
    }
}