package cc.mewcraft.orientation.novice

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

class NoviceManager {
    private var loaded: Boolean = false

    private val newbies: ConcurrentHashMap<UUID, Novice> = ConcurrentHashMap()

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

    private suspend fun Novice.isExpired(): Boolean {
        return NewbieConfig.noviceConditions.any { !it.test(this.uniqueId) }
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
            val newbie = NoviceFactory.createNewbie(player.uniqueId) ?: return

            newbies[player.uniqueId] = newbie
        }
    }
}