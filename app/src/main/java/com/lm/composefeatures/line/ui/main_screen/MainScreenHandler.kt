package com.lm.composefeatures.line.ui.main_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.motionEventSpy
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
        radius: Float
    )

    @Composable
    fun CheckForStrike(
        listPoints: SnapshotStateList<Offset>,
        radius: Float,
        eventOffset: Offset,
        offset: Offset,
        sinScaleX: Float,
        sinScaleY: Float, distance: Float, figure: Figures,
        onCheck: (Offset) -> Unit
    )

    @Composable
    fun BoxWithCanvas(
        listPoints: SnapshotStateList<Offset>,
        sinScaleX: Float,
        sinScaleY: Float,
        figure: Figures,
        offset: Offset,
        radius: Float,
        onEvent: (Int, Offset) -> Unit
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
            onCheck: (Offset) -> Unit
        ) = with(composeDependencies.mainScreenDeps()) {
            LaunchedEffect(eventOffset) {
                if (offset.check(eventOffset, radius + distance))
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
            LaunchedEffect(sinScaleX){
                if (eventOffset.x in listPoints.first().x..listPoints.last().x) {
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
            onEvent: (Int, Offset) -> Unit
        ) = Box(modifier = Modifier
            .fillMaxSize()
            .motionEventSpy { onEvent(it.action, Offset(it.x, it.y)) }
        ) {
            DrawFigure(listPoints, offset, radius)
        }

        @Composable
        override fun DrawFigure(
            listPoints: SnapshotStateList<Offset>,
            offset: Offset,
            radius: Float
        ) = Canvas(Modifier) {
            listPoints.forEach { draw(it, 10f) }
            draw(offset, radius)
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
                    LaunchedEffect(true){
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

        private fun Offset.check(eventOffset: Offset, radius: Float) =
            with(eventOffset) {
                x in this@check.x - radius..this@check.x + radius
                        && this@check.y in y - radius..this@check.y + radius
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
