package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.plugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class NoviceManager {
    private val newbies: ConcurrentHashMap<UUID, Novice> = ConcurrentHashMap()

    private val internalListener: InternalListener = InternalListener()

    val noviceSnapshot: Collection<Novice>
        get() {
            return newbies.values
        }

    fun getNewbie(uniqueId: UUID): Novice {
        return newbies.computeIfAbsent(uniqueId) { NoviceFactory.createNewbie(uniqueId) }
    }

    internal fun onLoad() {
        plugin.server.pluginManager.registerSuspendingEvents(internalListener, plugin)
    }

    internal fun onUnload() {
        HandlerList.unregisterAll(internalListener)
    }

    private inner class InternalListener : Listener {
        @EventHandler
        private fun onPlayerJoin(event: PlayerJoinEvent) {
            val player = event.player
            val uniqueId = player.uniqueId
            if (!newbies.containsKey(uniqueId)) {
                newbies[uniqueId] = NoviceFactory.createNewbie(uniqueId)
            }
        }

        @EventHandler
        private fun onPlayerQuit(event: PlayerQuitEvent) {
            val player = event.player
            val uniqueId = player.uniqueId
            newbies.remove(uniqueId)
        }
    }
}