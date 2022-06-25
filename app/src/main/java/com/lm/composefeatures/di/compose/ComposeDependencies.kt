package com.lm.composefeatures.di.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.lm.composefeatures.custom_slider.Figures
import javax.inject.Inject

interface ComposeDependencies {
    @Composable
    fun mainScreenDepsLocal(): MainDeps

    @Composable
    fun MainScreenDependencies(
        figure: Figures, radius: Float, figureLength: Int, distance: Float,
        content: @Composable () -> Unit
    )

    @Composable
    fun MainScreenDeps(loc: @Composable MainDeps.() -> Unit)

    class Base @Inject constructor(
        private val composeValues: ComposeValues
    ) : ComposeDependencies {
        @Composable
        override fun MainScreenDependencies(
            figure: Figures, radius: Float, figureLength: Int, distance: Float,
            content: @Composable () -> Unit
        ) {
            CompositionLocalProvider(
                local provides composeValues.mainScreenValues(figure, radius, figureLength, distance),
                content = content
            )
        }

        private val local by lazy {
            staticCompositionLocalOf<MainDeps> { error("No value provided") }
        }

        @Composable
        override fun MainScreenDeps(loc: @Composable MainDeps.() -> Unit) {
            loc(local.current)
        }

        @Composable
        override fun mainScreenDepsLocal() = local.current
    }
}
