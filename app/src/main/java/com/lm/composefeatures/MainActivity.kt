package com.lm.composefeatures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lm.composefeatures.di.compose.ComposeDependencies
import com.lm.composefeatures.line.ui.Screens
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var screens: Screens

    @Inject
    lateinit var composeDependencies: ComposeDependencies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        setContent {
            composeDependencies.MainScreenDependencies {
                screens.MainScreen()
            }
        }
    }
}
