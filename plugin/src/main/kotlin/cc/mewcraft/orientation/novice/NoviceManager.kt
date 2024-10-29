package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.plugin
import cc.mewcraft.playtime.Playtime
import cc.mewcraft.playtime.PlaytimeProvider
import cc.mewcraft.playtime.data.PlaytimeData
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.delay
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class NoviceManager {
    private var loaded: Boolean = false
    private val newbies: ConcurrentHashMap<UUID, Novice> = ConcurrentHashMap()

    private suspend fun refreshNewbies() {
        for (player in plugin.server.onlinePlayers) {
            val uniqueId: UUID = player.uniqueId
            if (newbies.containsKey(uniqueId)) {
                val novice: Novice = newbies[uniqueId]!!
                if (novice.isExpired()) {
                    newbies.remove(uniqueId)
                    continue
                }
            } else {
                NoviceFactory.createNewbie(uniqueId)?.let { newbies[uniqueId] = it }
            }
        }
    }

    val noviceSnapshot: Collection<Novice>
        get() {
            return newbies.values
        }

    fun hasNewbie(uniqueId: UUID): Boolean {
        return newbies.containsKey(uniqueId)
    }

    fun getNewbie(uniqueId: UUID): Novice? {
        return newbies[uniqueId]
    }

    suspend fun resetNewbie(uniqueId: UUID) {
        val playtime: Playtime = PlaytimeProvider.get()
        playtime.setPlaytime(uniqueId, PlaytimeData())
        NoviceFactory.createNewbie(uniqueId)?.let { newbies[uniqueId] = it }
    }

    internal fun onLoad() {
        loaded = true
        plugin.launch(plugin.asyncDispatcher) {
            while (loaded) {
                delay(1.toDuration(DurationUnit.SECONDS))

                refreshNewbies()
            }
        }
    }

    internal fun onUnload() {
        loaded = false
    }
}