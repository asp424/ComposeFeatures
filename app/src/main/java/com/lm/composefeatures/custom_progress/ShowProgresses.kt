package com.lm.composefeatures.custom_progress

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.sharp.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.unit.dp
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.lm.composefeatures.custom_progress.animation.enterLeftToRight
import com.lm.composefeatures.custom_progress.animation.exitLeftToRight
import com.lm.expandedcolumn.ExpandedColumn
import com.lm.expandedcolumn.ExpandedItem

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShowProgresses() {
    val navController = rememberAnimatedNavController()

    val list = listOf(
        ExpandedItem("Dangerous", Icons.Sharp.Dangerous, Yellow, Blue, Blue,
            onExpand = {
                if (navController.currentDestination?.route != "Dangerous")
                    navController.navigate("Dangerous")

            }),
        ExpandedItem("Ебасос", Icons.Sharp.Face, Red, White, White,
            onExpand = {
                if (navController.currentDestination?.route != "face")
                    navController.navigate("face")
            }),
        ExpandedItem("Facebook", Icons.Sharp.Facebook, Green, Gray, Gray,
            onExpand = {
                if (navController.currentDestination?.route != "Facebook")
                    navController.navigate("Facebook")
            }),
        ExpandedItem("Save", Icons.Sharp.Save, Blue, White, White,
            onExpand = {
                if (navController.currentDestination?.route != "Save")
                    navController.navigate("Save")

            }),
        ExpandedItem("Label", Icons.Sharp.Label, Magenta, Blue, Blue,
            onExpand = {
                if (navController.currentDestination?.route != "Label")
                    navController.navigate("Label")
            }),
        ExpandedItem("Hardware", Icons.Sharp.Hardware, Gray, White, White,
            onExpand = {
                if (navController.currentDestination?.route != "Hardware")
                    navController.navigate("Hardware")
            })
    )
    Column(modifier = Modifier.fillMaxSize().padding(top = 30.dp), Top, CenterHorizontally) {
        AnimatedNavHost(navController = navController, startDestination = "Placeholder",
            enterTransition = {
                enterLeftToRight
            }, exitTransition = {
                exitLeftToRight
            }
        ) {

            composable("Dangerous") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Center) {
                    Icon(
                        Icons.Rounded.Dangerous, null, modifier = Modifier
                            .size(200.dp)
                    )
                }
            }

            composable("face") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Center) {
                    Icon(
                        Icons.Rounded.Face, null, modifier = Modifier
                            .size(200.dp)
                    )
                }
            }
            composable("Facebook") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Center) {
                    Icon(
                        Icons.Rounded.Facebook, null, modifier = Modifier
                            .size(200.dp)
                    )
                }
            }

            composable("Save") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Center) {
                    Icon(
                        Icons.Rounded.Save, null, modifier = Modifier
                            .size(200.dp)
                    )
                }
            }
            composable("Label") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Center) {
                    Icon(
                        Icons.Rounded.Label, null, modifier = Modifier
                            .size(200.dp)
                    )
                }
            }
            composable("Hardware") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Center) {
                    Icon(
                        Icons.Rounded.Hardware, null, modifier = Modifier
                            .size(200.dp)
                    )
                }
            }

            composable("Placeholder") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Center) {
                    Icon(
                        Icons.Rounded.PhotoAlbum, null, modifier = Modifier
                            .size(200.dp)
                    )
                }
            }
        }
        ExpandedColumn(
            listItems = list, paddingBetweenItems = 10.dp, itemSize = 40.dp, modifier = Modifier
                .padding(top = 50.dp, start = 40.dp)
                .height(400.dp).fillMaxWidth(), expandedItemWidth = 120.dp, onUnLongPress = {
                navController.navigate("Placeholder")
            }, contentPadding = 5.dp
        )
    }
}


