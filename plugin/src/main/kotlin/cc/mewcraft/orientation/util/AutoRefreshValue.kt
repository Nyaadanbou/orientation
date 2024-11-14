package cc.mewcraft.orientation.util

import cc.mewcraft.orientation.novice.NoviceRefreshListener
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
    private val noviceRefreshListeners: CopyOnWriteArrayList<NoviceRefreshListener<T>> = CopyOnWriteArrayList()

    init {
        startRefreshing()
    }

    private fun startRefreshing() {
        refreshJob?.cancel()
        refreshJob = scope.launch {
            while (isActive) {
                cachedValue = valueProvider()
                noviceRefreshListeners.forEach { it.onRefresh(cachedValue!!) }
                delay(expireTime)
            }
        }
    }

    fun getValue(): T? = cachedValue

    suspend fun stopRefreshing() {
        noviceRefreshListeners.forEach { it.onDestroy() }
        noviceRefreshListeners.clear()
        refreshJob?.cancel()
        scope.cancel()
    }

    fun addRefreshListener(listener: NoviceRefreshListener<T>) {
        noviceRefreshListeners.add(listener)
    }

    fun removeRefreshListener(listener: NoviceRefreshListener<T>) {
        noviceRefreshListeners.remove(listener)
    }

    suspend fun refreshValue(): T {
        cachedValue = valueProvider()
        noviceRefreshListeners.forEach { it.onRefresh(cachedValue!!) }
        startRefreshing()
        return cachedValue!!
    }

    operator fun getValue(thisRef: Any?, property: Any?): T? = cachedValue
}