package com.lm.composefeatures.di.compose

import androidx.compose.runtime.Composable
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
                        width = 80f,
                        height = screenHeightDp.dp.toPx() / 3
                    )
                }
            }
    }
}