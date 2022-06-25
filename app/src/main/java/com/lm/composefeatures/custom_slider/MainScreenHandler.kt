package com.lm.composefeatures.custom_slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import com.lm.composefeatures.di.compose.ComposeDependencies
import javax.inject.Inject

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

    @Composable
    fun Debug(
        offset: Offset, scaleX: Float, scaleY: Float,
        onClick: (Boolean) -> Unit, onScaleX: (Float) -> Unit,
        onScaleY: (Float) -> Unit
    )

    class Base @Inject constructor(
        private val composeDependencies: ComposeDependencies,
        private val autoMoveBall: MoveBall,
        private val debugWidgets: DebugWidgets,
        private val handlerUtils: HandlerUtils
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
            with(handlerUtils) {
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
                    if (!offset.check(
                            eventOffset, radius + distance, strike) || action == 1
                    ) onAction(false)
                }
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
            with(handlerUtils) {
                Canvas(Modifier) {
                    listPoints.forEach { draw(it, 10f) }
                }

                Box(
                    Modifier
                        .boxMod(LocalDensity.current, offset, radius)
                        .pointerInput(Unit) { detectTapGestures(onPress = { onPress() }) }
                ) { Canvas(Modifier) { draw(Offset(radius, radius), radius) } }
            }
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
                with(handlerUtils) {
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
        }

        @Composable
        override fun AutoMoveBall(
            listPoints: SnapshotStateList<Offset>,
            start: Boolean,
            onTick: (Offset) -> Unit,
            onEnd: () -> Unit
        ) = autoMoveBall.AutoMoveBallByTimer(listPoints, start, onTick = { onTick(it) })
        { onEnd() }

        @Composable
        override fun Debug(
            offset: Offset,
            scaleX: Float,
            scaleY: Float,
            onClick: (Boolean) -> Unit,
            onScaleX: (Float) -> Unit,
            onScaleY: (Float) -> Unit
        ) = debugWidgets.Debug(offset, scaleX, scaleY, onClick, onScaleX, onScaleY)
    }
}
