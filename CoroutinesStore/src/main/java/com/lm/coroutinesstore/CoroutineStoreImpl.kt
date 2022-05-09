package com.lm.coroutinesstore

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

internal class CoroutineStoreImpl: CoroutineStore {

    override fun io(work: suspend (CoroutineScope.() -> Unit)) =
        IO.scope.launch(start = CoroutineStart.LAZY) { work() }

    override fun main(work: suspend (CoroutineScope.() -> Unit)) =
        Main.scope.launch(start = CoroutineStart.LAZY) { work() }

    override fun coroutine(
        dispatcher: CoroutineDispatcher,
        scope: CoroutineScope,
        key: String,
        work: suspend (CoroutineScope.() -> Unit)
    ): Job {
        actives[key]?.cancel()
        return scope.launch(dispatcher, CoroutineStart.LAZY)
        { work() }.apply { actives[key] = this }
    }

    override fun isActive(key: String) = actives[key]?.isActive

    override fun cancel(key: String) { actives[key]?.cancel() }

    override fun cancelAll() { io {
        with(actives) { forEach { (key, _) -> get(key)?.cancel() } }
    }.start() }

    private val actives by lazy { hashMapOf<String, Job>() }

    private val CoroutineContext.scope get() = CoroutineScope(this)

}



