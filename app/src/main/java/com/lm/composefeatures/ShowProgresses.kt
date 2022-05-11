package com.lm.composefeatures

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lm.composefeatures.cells.Visibility
import com.lm.expandedcolumn.ExpandedColumn
import com.lm.expandedcolumn.ExpandedItem
import com.lm.fantasticprogress.CircleProgress
import com.lm.fantasticprogress.ProgressCircleType

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShowProgresses() {
    var text by remember { mutableStateOf("") }
        var visible by remember { mutableStateOf(false) }
        var visible1 by remember { mutableStateOf(false) }
        var visible2 by remember { mutableStateOf(false) }
        val list = listOf(
            ExpandedItem(
                "Atom progress", Icons.Sharp.Check, Color.Yellow, Color.Blue, Color.Blue,
                onExpand = {
                    visible1 = false
                    visible2 = false
                    visible = true
                    text = "Atom progress"
                    text = "Atom progress"
                }),
            ExpandedItem(
                "Fast progress", Icons.Sharp.Check, Color.Red, Color.White, Color.White,
                onExpand = {
                    visible = false
                    visible2 = false
                    visible1 = true
                    text = "Fast progress"
                }),
            ExpandedItem(
                "Middle custom progress", Icons.Sharp.Check, Color.Green, Color.Gray, Color.Gray,
                onExpand = {
                    visible1 = false
                    visible = false
                    visible2 = true
                    text = "Middle custom progress"
                })
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
        ) {
            Visibility(visible = visible) {
                CircleProgress(type = ProgressCircleType.Atom, mazSize = 3f)
            }
            Visibility(visible = visible1) {
                CircleProgress(type = ProgressCircleType.Fast, mazSize = 3f)
            }
            Visibility(visible = visible2) {
                CircleProgress(
                    type = ProgressCircleType.Middle,
                    mazSize = 3f,
                    minSize = 1f
                )
            }
            Text(text, modifier = Modifier.padding(top = 80.dp))
        }

        ExpandedColumn(
            listItems = list, paddingBetweenItems = 10.dp, itemSize = 40.dp,
            modifier = Modifier
                .padding(start = 50.dp, top = 100.dp)
                .size(400.dp), expandedItemWidth = 200.dp, onUnLongPress = {
                visible = false
                visible1 = false
                visible2 = false
                text = ""
            }
        )
    }

