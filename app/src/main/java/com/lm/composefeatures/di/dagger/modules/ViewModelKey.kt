package com.lm.composefeatures.di.dagger.modules

import androidx.lifecycle.ViewModel
import dagger.MapKey
import javax.inject.Singleton
import kotlin.reflect.KClass

@MapKey
@Singleton
@Target(AnnotationTarget.FUNCTION)
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
