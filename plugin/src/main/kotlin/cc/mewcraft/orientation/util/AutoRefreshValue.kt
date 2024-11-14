package cc.mewcraft.orientation.util

import cc.mewcraft.orientation.novice.NoviceRefreshListener
import kotlinx.coroutines.*
import net.kyori.adventure.key.Key
import java.util.concurrent.ConcurrentHashMap
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
    private val noviceRefreshListeners: ConcurrentHashMap<Key, NoviceRefreshListener<T>> = ConcurrentHashMap()

    init {
        startRefreshing()
    }

    private fun startRefreshing() {
        refreshJob?.cancel()
        refreshJob = scope.launch {
            while (isActive) {
                cachedValue = valueProvider()
                noviceRefreshListeners.forEach { it.value.onRefresh(cachedValue!!) }
                delay(expireTime)
            }
        }
    }

    fun getValue(): T? = cachedValue

    suspend fun stopRefreshing() {
        noviceRefreshListeners.forEach { it.value.onDestroy() }
        noviceRefreshListeners.clear()
        refreshJob?.cancel()
        scope.cancel()
    }

    fun addRefreshListener(key: Key, listener: NoviceRefreshListener<T>) {
        noviceRefreshListeners[key] = listener
    }

    fun removeRefreshListener(key: Key) {
        noviceRefreshListeners.remove(key)
    }

    suspend fun refreshValue(): T {
        cachedValue = valueProvider()
        noviceRefreshListeners.forEach { it.value.onRefresh(cachedValue!!) }
        startRefreshing()
        return cachedValue!!
    }

    operator fun getValue(thisRef: Any?, property: Any?): T? = cachedValue
}