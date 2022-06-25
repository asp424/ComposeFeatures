package com.lm.composefeatures.custom_slider

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import com.lm.composefeatures.di.compose.ComposeDependencies
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sin


interface HandlerUtils {

    @Composable
    fun Modifier.boxMod(radius: Float): Modifier

    fun DrawScope.draw(radius: Float, offset: Offset)

    @Composable
    fun Float.Sinus(): Offset

    @Composable
    fun Float.Circle(): Offset

    @Composable
    fun CompareOffsets(radius: Float): Boolean

    @Composable
    fun CheckInList(): Boolean

    @Composable
    fun CheckInListAndGetSinusByEventX(figure: Figures)

    @Composable
    fun Float.SinusByEventX(): Offset

    class Base @Inject constructor(
        private val composeDependencies: ComposeDependencies
    ) : HandlerUtils {

        @Composable
        override fun Modifier.boxMod(radius: Float) =
            with(composeDependencies.mainScreenDepsLocal()) {
                with(LocalDensity.current) {
                    offset(
                        offset.x.toDp() - radius.toDp(),
                        offset.y.toDp() - radius.toDp()
                    ).size(radius.toDp() * 2)
                }
            }

        override fun DrawScope.draw(radius: Float, offset: Offset) =
            drawCircle(Black, radius, offset)

        @Composable
        override fun Float.Sinus(): Offset =
            with(composeDependencies.mainScreenDepsLocal()) {
            Offset(this@Sinus * scaleX + width, scaleY * sin(this@Sinus) + height)
        }

        @Composable
        override fun Float.Circle() = with(composeDependencies.mainScreenDepsLocal()){
            Offset(scaleX * sin(this@Circle) + width, scaleX * cos(this@Circle) + height)
        }

        @Composable
        override fun CompareOffsets(radius: Float) =
            with(composeDependencies.mainScreenDepsLocal()) {
                offset.x in eventOffset.x.minus(radius)..eventOffset.x.plus(radius)
                        && offset.y in eventOffset.y.minus(radius)..
                        eventOffset.y.plus(radius) && strike
            }

        @Composable
        override fun CheckInList() =
            with(composeDependencies.mainScreenDepsLocal()) {
            eventOffset.x in listPoints.first().x..listPoints.last().x
        }

        @Composable
        override fun Float.SinusByEventX() =
            with(composeDependencies.mainScreenDepsLocal()) {
                ((this@SinusByEventX - width) / scaleX).Sinus()
            }

        @Composable
        override fun CheckInListAndGetSinusByEventX(figure: Figures) =
            composeDependencies.MainScreenDeps {
                if (CheckInList()) {
                    when (figure) {
                        Figures.SINUS -> { eventOffset.x.SinusByEventX().setOffset }
                        Figures.CIRCLE -> Unit
                    }
                }
            }
    }
}
