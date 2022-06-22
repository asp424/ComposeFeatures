package com.lm.composefeatures.line.ui.main_screen

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import javax.inject.Inject
import kotlin.math.sin

interface MainScreenHandler {

    fun Float.offset(width: Float, height: Float): Offset

    fun Float.offsetRect(width: Float, height: Float, rectSize: Float): Offset

    @Composable
    fun Canvas(
        listPoints: SnapshotStateList<Rect>, width: Float,
        height: Float, rectSize: Float, radius: Float, ball: Float
    )

    fun MotionEvent.checkForStrike(
        width: Float,
        height: Float,
        ball: Float,
        radius: Float
    ): Boolean

    class Base @Inject constructor(
    ) : MainScreenHandler {

        override fun Float.offset(width: Float, height: Float) = Offset(
            (this * 80) + width,
            (360 * sin(this)) + height
        )

        override fun Float.offsetRect(width: Float, height: Float, rectSize: Float) = Offset(
            (this * 80) + width - rectSize / 2,
            (360 * sin(this)) + height - rectSize / 2
        )

        override fun MotionEvent.checkForStrike(
            width: Float,
            height: Float,
            ball: Float,
            radius: Float
        ) = x in ((ball * 80) + width) - radius..
                ((ball * 80) + width) + radius && y in
                360 * sin(ball) + height - radius..360 * sin(ball) + height + radius && action == 0

        @Composable
        override fun Canvas(
            listPoints: SnapshotStateList<Rect>, width: Float,
            height: Float, rectSize: Float, radius: Float, ball: Float
        ) = Canvas(
            Modifier

        ) {
            listPoints.forEach {
                drawRect(
                    topLeft = it.center.x.offsetRect(width, height, rectSize),
                    color = Color.Gray, size = Size(rectSize, rectSize)
                )

                drawCircle(
                    radius = 10f,
                    center = it.center.x.offset(width, height),
                    color = Color.Black
                )
            }
            drawCircle(
                radius = radius,
                center = ball.offset(width, height),
                color = Color.Black
            )
        }
    }
}