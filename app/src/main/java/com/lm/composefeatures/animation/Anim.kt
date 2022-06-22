package com.lm.composefeatures.animation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.internal.composableLambda
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder

const val speed = 300
@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<NavBackStackEntry>.enterUpToDown
    get() = slideIntoContainer(AnimatedContentScope.SlideDirection.Down, tween(speed))

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<NavBackStackEntry>.enterDownToUp
    get() = slideIntoContainer(AnimatedContentScope.SlideDirection.Up, tween(speed))

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<NavBackStackEntry>.enterLeftToRight
    get() = slideIntoContainer(AnimatedContentScope.SlideDirection.Right, tween(speed))

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<NavBackStackEntry>.enterRightToLeft
    get() = slideIntoContainer(AnimatedContentScope.SlideDirection.Left, tween(speed))

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<NavBackStackEntry>.exitDownToUp
    get() = slideOutOfContainer(AnimatedContentScope.SlideDirection.Down,
        animationSpec = tween(speed))

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<NavBackStackEntry>.exitUpToDown
    get() = slideOutOfContainer(AnimatedContentScope.SlideDirection.Up,
        animationSpec = tween(speed))

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<NavBackStackEntry>.exitLeftToRight
    get() = slideOutOfContainer(AnimatedContentScope.SlideDirection.Right,
        animationSpec = tween(speed))

@OptIn(ExperimentalAnimationApi::class)
val AnimatedContentScope<NavBackStackEntry>.exitRightToLeft
    get() = slideOutOfContainer(AnimatedContentScope.SlideDirection.Left,
        animationSpec = tween(speed))

