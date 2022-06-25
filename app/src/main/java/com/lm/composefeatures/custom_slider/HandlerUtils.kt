package com.lm.composefeatures.custom_slider

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import javax.inject.Inject
import kotlin.math.cos
import kotlin.math.sin


interface HandlerUtils {

    fun Modifier.boxMod(
        density: Density, offset: Offset, radius: Float
    ): Modifier

    fun DrawScope.draw(
        offset: Offset,
        radius: Float
    )

    fun Float.sinus(
        width: Float,
        scaleX: Float,
        height: Float,
        scaleY: Float
    ): Offset

    fun Float.circle(
        width: Float,
        scaleX: Float,
        height: Float
    ): Offset

    fun Offset.check(eventOffset: Offset, radius: Float, strike: Boolean): Boolean

    class Base @Inject constructor(): HandlerUtils {

        override fun Modifier.boxMod(
            density: Density, offset: Offset, radius: Float
        ) = with(density) {
            offset(
                offset.x.toDp() - radius.toDp(),
                offset.y.toDp() - radius.toDp()
            ).size(radius.toDp() * 2)
        }

        override fun DrawScope.draw(
            offset: Offset,
            radius: Float
        ) = drawCircle(Color.Black, radius, offset)

        override fun Float.sinus(
            width: Float,
            scaleX: Float,
            height: Float,
            scaleY: Float
        ) = Offset(this * scaleX + width, scaleY * sin(this) + height)

        override fun Float.circle(
            width: Float,
            scaleX: Float,
            height: Float
        ) = Offset(scaleX * sin(this) + width, scaleX * cos(this) + height)

        override fun Offset.check(eventOffset: Offset, radius: Float, strike: Boolean) =
            with(eventOffset) {
                x in this@check.x - radius..this@check.x + radius
                        && y in this@check.y - radius..this@check.y + radius && strike
            }
    }
}