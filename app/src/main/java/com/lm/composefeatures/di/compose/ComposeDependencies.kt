package com.lm.composefeatures.di.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import javax.inject.Inject

interface ComposeDependencies {

    @Composable
    fun MainDependencies(content: @Composable () -> Unit)

    @Composable
    fun mainDeps(): MainDeps

    class Base @Inject constructor(
        private val composeValues: ComposeValues
    ) : ComposeDependencies {

        @Composable
        override fun MainDependencies(content: @Composable () -> Unit) {
            CompositionLocalProvider(
                local provides composeValues.baseValues(), content = content
            )
        }

        private val local by lazy {
            staticCompositionLocalOf<MainDeps> { error("No value provided") }
        }

        @Composable
        override fun mainDeps() = local.current
    }
}
