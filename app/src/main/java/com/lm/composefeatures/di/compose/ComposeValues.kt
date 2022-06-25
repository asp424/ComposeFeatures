package com.lm.composefeatures.di.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.lm.composefeatures.custom_slider.Figures
import javax.inject.Inject

interface ComposeValues {

    @Composable
    fun mainScreenValues(
        figure: Figures,
        radius: Float,
        figureLength: Int,
        distance: Float
    ): MainDeps

    class Base @Inject constructor() : ComposeValues {

        @Composable
        override fun mainScreenValues(
            figure: Figures,
            radius: Float,
            figureLength: Int,
            distance: Float
        ) =
            with(LocalConfiguration.current) {
                with(LocalDensity.current) {
                    MainDeps(
                        width = 80f,
                        height = screenHeightDp.dp.toPx() / 3,
                        _offset = remember { mutableStateOf(Offset.Zero) },
                        _scaleX = remember { mutableStateOf(90f) },
                        _scaleY = remember { mutableStateOf(20f) },
                        _eventOffset = remember { mutableStateOf(Offset.Zero) },
                        _listPoints = remember { mutableStateListOf() },
                        _action = remember { mutableStateOf(-1) },
                        _startMove = remember { mutableStateOf(false) },
                        _strike = remember { mutableStateOf(false) },
                    ).apply {
                        figure.setFigure; radius.setRadius; figureLength.setFigureLength
                        distance.setDistance
                    }
                }
            }
    }
}