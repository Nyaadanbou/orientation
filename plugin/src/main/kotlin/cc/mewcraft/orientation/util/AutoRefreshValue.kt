package cc.mewcraft.orientation.util

import kotlinx.coroutines.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

class AutoRefreshValue<T>(
    private val expireTime: Duration,
    coroutineContext: CoroutineContext = Dispatchers.Default,
    private val valueProvider: suspend () -> T,
) {
    private var cachedValue: T? = null
    private val scope = CoroutineScope(coroutineContext + CoroutineName("Auto Refresh Value") + SupervisorJob())
    private var refreshJob: Job? = null
    private val refreshListeners: CopyOnWriteArrayList<RefreshListener<T>> = CopyOnWriteArrayList()

    init {
        startRefreshing()
    }

    private fun startRefreshing() {
        refreshJob?.cancel()
        refreshJob = scope.launch {
            while (isActive) {
                cachedValue = valueProvider()
                refreshListeners.forEach { it.onRefresh(cachedValue!!) }
                delay(expireTime)
            }
        }
    }

    fun getValue(): T? = cachedValue

    suspend fun stopRefreshing() {
        refreshListeners.forEach { it.onDestroy() }
        refreshListeners.clear()
        refreshJob?.cancel()
        scope.cancel()
    }

    fun addRefreshListener(listener: RefreshListener<T>) {
        refreshListeners.add(listener)
    }

    suspend fun refreshValue(): T {
        cachedValue = valueProvider()
        refreshListeners.forEach { it.onRefresh(cachedValue!!) }
        startRefreshing()
        return cachedValue!!
    }

    operator fun getValue(thisRef: Any?, property: Any?): T? = cachedValue
}

fun <T> RefreshListener(
    onRefresh: suspend (T) -> Unit = { },
    onExpire: suspend () -> Unit = { },
) : RefreshListener<T> {
    return object : RefreshListener<T> {
        override suspend fun onRefresh(value: T) {
            onRefresh(value)
        }

        override suspend fun onDestroy() {
            onExpire()
        }
    }
}

interface RefreshListener<T> {
    suspend fun onRefresh(value: T)

    suspend fun onDestroy()
}