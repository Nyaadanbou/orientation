package cc.mewcraft.orientation.novice

fun <T> NoviceRefreshListener(
    onRefresh: suspend (T) -> Unit = { },
    onExpire: suspend () -> Unit = { },
) : NoviceRefreshListener<T> {
    return object : NoviceRefreshListener<T> {
        override suspend fun onRefresh(value: T) {
            onRefresh(value)
        }

        override suspend fun onDestroy() {
            onExpire()
        }
    }
}

interface NoviceRefreshListener<T> {
    suspend fun onRefresh(value: T)

    suspend fun onDestroy()
}