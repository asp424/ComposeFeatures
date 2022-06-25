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
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.lm.composefeatures.core.log
import com.lm.composefeatures.di.compose.ComposeDependencies
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sin

interface MainScreenHandler {

    @Composable
    fun InitListPoints(
        listPoints: SnapshotStateList<Offset>,
        scaleX: Float,
        scaleY: Float,
        figure: Figures,
        onAddFloat: (Offset) -> Unit, onInit: () -> Unit
    )

    @Composable
    fun DrawFigure(
        listPoints: SnapshotStateList<Offset>,
        offset: Offset,
        radius: Float,
        onPress: () -> Unit
    )

    @Composable
    fun CheckForStrike(
        listPoints: SnapshotStateList<Offset>,
        radius: Float,
        eventOffset: Offset,
        offset: Offset,
        scaleX: Float,
        scaleY: Float,
        distance: Float,
        figure: Figures,
        strike: Boolean,
        action: Int,
        onCheck: (Offset) -> Unit, onAction: (Boolean) -> Unit
    )

    @Composable
    fun BoxWithCanvas(
        listPoints: SnapshotStateList<Offset>,
        scaleX: Float,
        scaleY: Float,
        figure: Figures,
        offset: Offset,
        radius: Float,
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
            scaleX: Float,
            scaleY: Float,
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
                                            scaleX).sinus(width, scaleX, height, scaleY)
                                Figures.CIRCLE ->
                                    ((eventOffset.x - width) /
                                            scaleX).circle(
                                        width, scaleX, height
                                    )
                            }
                        )
                }
                if (!offset.check(eventOffset, radius + distance, strike) || action == 1)
                    onAction(false)
            }
        }

        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        override fun BoxWithCanvas(
            listPoints: SnapshotStateList<Offset>,
            scaleX: Float,
            scaleY: Float,
            figure: Figures,
            offset: Offset,
            radius: Float,
            onEvent: (Int, Offset) -> Unit,
            onPress: () -> Unit
        ) = Box(modifier = Modifier
            .fillMaxSize()
            .motionEventSpy { onEvent(it.action, Offset(it.x, it.y)) }
        ) {
            DrawFigure(listPoints, offset, radius, onPress)
        }

        @Composable
        override fun DrawFigure(
            listPoints: SnapshotStateList<Offset>,
            offset: Offset,
            radius: Float,
            onPress: () -> Unit
        ) {
            Canvas(Modifier) {
                listPoints.forEach { draw(it, 10f) }
            }

            Box(Modifier.boxMod(LocalDensity.current, offset, radius)
                    .pointerInput(Unit) { detectTapGestures(onPress = { onPress() }) }
            ) { Canvas(Modifier) { draw(Offset(radius, radius), radius) } }
        }


        @Composable
        override fun InitListPoints(
            listPoints: SnapshotStateList<Offset>,
            scaleX: Float,
            scaleY: Float,
            figure: Figures,
            onAddFloat: (Offset) -> Unit, onInit: () -> Unit
        ) {
            composeDependencies.mainScreenDeps().apply {
                listPoints.apply {
                    LaunchedEffect(
                        scaleX, scaleY
                    ) {
                        clear()
                        (0..1000).onEach {
                            (it * 0.01f).also { k ->
                                when (figure) {
                                    Figures.SINUS -> k.sinus(
                                        width, scaleX, height, scaleY
                                    )
                                    Figures.CIRCLE -> k.circle(
                                        width, scaleX, height
                                    )
                                }.apply {
                                    add(this)
                                    onAddFloat(get(size / 2))
                                }
                                if (it == 300) onInit()
                            }
                        }
                    }
                }
            }
        }

        private fun Modifier.boxMod(
            density: Density, offset: Offset, radius: Float
        ) = with(density) {
            offset(
                offset.x.toDp() - radius.toDp(),
                offset.y.toDp() - radius.toDp()
            ).size(radius.toDp() * 2)
        }

        private fun DrawScope.draw(
            offset: Offset,
            radius: Float
        ) = drawCircle(Black, radius, offset)

        private fun Float.sinus(
            width: Float,
            scaleX: Float,
            height: Float,
            scaleY: Float
        ) = Offset(this * scaleX + width, scaleY * sin(this) + height)

        private fun Float.circle(
            width: Float,
            scaleX: Float,
            height: Float
        ) = Offset(scaleX * sin(this) + width, scaleX * cos(this) + height)

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
