package com.lm.composefeatures.line.ui.main_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.lm.composefeatures.di.compose.ComposeDependencies
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sin

interface MainScreenHandler {

    @Composable
    fun InitListPoints(
        listPoints: SnapshotStateList<Offset>,
        sinScaleX: Float,
        sinScaleY: Float,
        figure: Figures,
        onAddFloat: (Offset) -> Unit, onInit: () -> Unit
    )

    @Composable
    fun DrawFigure(
        listPoints: SnapshotStateList<Offset>,
        offset: Offset,
        radius: Float,
        distance: Float,
        onPress: () -> Unit
    )

    @Composable
    fun CheckForStrike(
        listPoints: SnapshotStateList<Offset>,
        radius: Float,
        eventOffset: Offset,
        offset: Offset,
        sinScaleX: Float,
        sinScaleY: Float,
        distance: Float,
        figure: Figures,
        strike: Boolean,
        action: Int,
        onCheck: (Offset) -> Unit, onAction: (Boolean) -> Unit
    )

    @Composable
    fun BoxWithCanvas(
        listPoints: SnapshotStateList<Offset>,
        sinScaleX: Float,
        sinScaleY: Float,
        figure: Figures,
        offset: Offset,
        radius: Float,
        distance: Float,
        onEvent: (Int, Offset) -> Unit,
        onPress: () -> Unit
    )

    @Composable
    fun AutoMoveBall(
        listPoints: SnapshotStateList<Offset>, start: Boolean, onTick: (Offset) -> Unit,
        onEnd: () -> Unit
    )

    class Base @Inject constructor(
        private val composeDependencies: ComposeDependencies,
        private val autoMoveBall: MoveBall
    ) : MainScreenHandler {

        @Composable
        override fun CheckForStrike(
            listPoints: SnapshotStateList<Offset>,
            radius: Float,
            eventOffset: Offset,
            offset: Offset,
            sinScaleX: Float,
            sinScaleY: Float,
            distance: Float,
            figure: Figures,
            strike: Boolean,
            action: Int,
            onCheck: (Offset) -> Unit, onAction: (Boolean) -> Unit
        ) = with(composeDependencies.mainScreenDeps()) {
            LaunchedEffect(eventOffset) {
                if (strike) {
                    if (eventOffset.x in listPoints.first().x..listPoints.last().x)
                        onCheck(
                            when (figure) {
                                Figures.SINUS ->
                                    ((eventOffset.x - width) /
                                            sinScaleX).sinus(width, sinScaleX, height, sinScaleY)
                                Figures.CIRCLE ->
                                    ((eventOffset.x - width) /
                                            sinScaleX).circle(width, sinScaleX, height)
                            }
                        )
                }
                if (!offset.check(eventOffset, radius + distance, strike)) onAction(false)
            }
            LaunchedEffect(action) {
                if (action == 1) onAction(false)
            }
        }

        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        override fun BoxWithCanvas(
            listPoints: SnapshotStateList<Offset>,
            sinScaleX: Float,
            sinScaleY: Float,
            figure: Figures,
            offset: Offset,
            radius: Float,
            distance: Float,
            onEvent: (Int, Offset) -> Unit,
            onPress: () -> Unit
        ) = Box(modifier = Modifier
            .fillMaxSize()
            .motionEventSpy { onEvent(it.action, Offset(it.x, it.y)) }
        ) {
            DrawFigure(listPoints, offset, radius, distance, onPress)
        }

        @Composable
        override fun DrawFigure(
            listPoints: SnapshotStateList<Offset>,
            offset: Offset,
            radius: Float,
            distance: Float,
            onPress: () -> Unit
        ) {
            Canvas(Modifier) {
                listPoints.forEach { draw(it, 10f) }
            }
            LocalDensity.current.apply {
                Box(
                    Modifier
                        .size(radius.toDp() * 2)
                        .offset(
                            offset.x.toDp() - radius.toDp(),
                            offset.y.toDp() - radius.toDp()
                        )
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    onPress()
                                }
                            )
                        }.background(Green)
                ) {
                    Canvas(Modifier) {
                        draw(Offset(radius, radius), radius)
                    }
                }
            }
        }

        private fun DrawScope.draw(
            offset: Offset,
            radius: Float
        ) = drawCircle(Black, radius, offset)

        @Composable
        override fun InitListPoints(
            listPoints: SnapshotStateList<Offset>,
            sinScaleX: Float,
            sinScaleY: Float,
            figure: Figures,
            onAddFloat: (Offset) -> Unit, onInit: () -> Unit
        ) {
            composeDependencies.mainScreenDeps().apply {
                listPoints.apply {
                    LaunchedEffect(sinScaleX, sinScaleY) {
                        clear()
                        (0..1000).onEach {
                            (it * 0.01f).also { k ->
                                when (figure) {
                                    Figures.SINUS -> k.sinus(width, sinScaleX, height, sinScaleY)
                                    Figures.CIRCLE -> k.circle(width, sinScaleX, height)
                                }.apply {
                                    add(this)
                                }
                                if (it == 300) onInit()
                            }
                        }
                    }
                    LaunchedEffect(true) {
                        onAddFloat(get(0))
                    }
                }
            }
        }

        private fun Float.sinus(
            width: Float,
            sinScaleX: Float,
            height: Float,
            sinScaleY: Float
        ) = Offset(this * sinScaleX + width, sinScaleY * sin(this) + height)

        private fun Float.circle(width: Float, sinScaleX: Float, height: Float) =
            Offset(sinScaleX * sin(this) + width, sinScaleX * cos(this) + height)

        private fun Offset.check(eventOffset: Offset, radius: Float, strike: Boolean) =
            with(eventOffset) {
                x in this@check.x - radius..this@check.x + radius
                        && y in this@check.y - radius..this@check.y + radius && strike
            }

        @Composable
        override fun AutoMoveBall(
            listPoints: SnapshotStateList<Offset>,
            start: Boolean,
            onTick: (Offset) -> Unit,
            onEnd: () -> Unit
        ) = autoMoveBall.AutoMoveBallByTimer(listPoints, start, onTick = { onTick(it) })
        { onEnd() }
    }
}
