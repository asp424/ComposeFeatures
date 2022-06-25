package com.lm.composefeatures.custom_slider

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import javax.inject.Inject

interface DebugWidgets {

    @Composable
    fun Debug(
        offset: Offset, scaleX: Float, scaleY: Float,
        onClick: (Boolean) -> Unit, onScaleX: (Float) -> Unit,
        onScaleY: (Float) -> Unit
    )

    class Base @Inject constructor() : DebugWidgets {

        @Composable
        override fun Debug(
            offset: Offset, scaleX: Float, scaleY: Float,
            onClick: (Boolean) -> Unit, onScaleX: (Float) -> Unit,
            onScaleY: (Float) -> Unit
        ) {
            val buttonText by remember { mutableStateOf("Go") }
            var buttonEnable by remember { mutableStateOf(true) }

            LaunchedEffect(scaleX){
                if (scaleX == 90f) buttonEnable = true
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp, start = 40.dp, end = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Slider(
                    value = scaleY,
                    onValueChange = {
                        onScaleY(it)
                    },
                    valueRange = (0f..100f),
                    modifier = Modifier
                )
                Slider(
                    value = scaleX, onValueChange = {
                        onScaleX(it)
                    }, valueRange = (0f..90f), modifier = Modifier
                )

                Button(onClick = { onClick(true); buttonEnable = false }, enabled = buttonEnable)
                { Text(text = buttonText) }
            }
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Icon(
                    Icons.Default.Abc, null,
                    modifier = Modifier.size(offset.x.dp / 5)
                )
            }
        }
    }
}