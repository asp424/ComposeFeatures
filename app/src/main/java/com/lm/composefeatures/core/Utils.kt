package com.lm.composefeatures.core

import android.util.Log

val <T> T.log get() = Log.d("My", toString())