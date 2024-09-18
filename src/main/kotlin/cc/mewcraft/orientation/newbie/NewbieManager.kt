package cc.mewcraft.orientation.newbie

import cc.mewcraft.orientation.config.NewbieConfig
import cc.mewcraft.orientation.plugin
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import kotlinx.coroutines.delay
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class NewbieManager {
    private var loaded: Boolean = false

    private val newbies: ConcurrentHashMap<UUID, Newbie> = ConcurrentHashMap()

    private val listener: NewbieManagerListener = NewbieManagerListener()

    init {
        plugin.server.pluginManager.registerSuspendingEvents(listener, plugin)
    }

    private suspend fun refreshNewbies() {
        val iterator = newbies.iterator()

        while (iterator.hasNext()) {
            val newbie = iterator.next().value
            if (newbie.isExpired()) {
                iterator.remove()
            }
        }
    }

    private suspend fun Newbie.isExpired(): Boolean {
        return NewbieConfig.newbieConditions.any { !it.test(this.uniqueId) }
    }

    val newbieSnapshot: Collection<Newbie>
        get() {
            return newbies.values
        }

    fun hasNewbie(uniqueId: UUID): Boolean {
        return newbies.containsKey(uniqueId)
    }

    fun getNewbie(uniqueId: UUID): Newbie? {
        return newbies[uniqueId]
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
        HandlerList.unregisterAll(listener)
    }

    private inner class NewbieManagerListener : Listener {
        @EventHandler
        private suspend fun onPlayerJoin(event: PlayerJoinEvent) {
            val player = event.player
            val newbie = NewbieFactory.createNewbie(player.uniqueId) ?: return

            newbies[player.uniqueId] = newbie
        }
    }
}