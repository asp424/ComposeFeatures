package com.lm.coroutinesstore

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import java.util.*

interface CoroutineStore {

    fun io(work: suspend (CoroutineScope.() -> Unit)): Job

    fun main(work: suspend (CoroutineScope.() -> Unit)): Job

    fun coroutine(
        dispatcher: CoroutineDispatcher,
        scope: CoroutineScope,
        key: String,
        work: suspend (CoroutineScope.() -> Unit)
    ): Job

    fun isActive(key: String): Boolean?

    fun cancel(key: String)

    fun cancelAll()

}
