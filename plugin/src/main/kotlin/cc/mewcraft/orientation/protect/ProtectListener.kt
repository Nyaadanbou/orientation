package cc.mewcraft.orientation.protect

import cc.mewcraft.orientation.extension.toNewbie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

object ProtectListener : Listener {
    @EventHandler
    private suspend fun onPlayerDeath(event: PlayerDeathEvent) {
        val newbie = event.player.toNewbie() ?: return
        if (newbie.isExpired())
            return

        if (newbie.protects.hasProtect(Protect.KEEP_INV)) {
            event.keepInventory = true
            event.keepLevel = true
            event.droppedExp = 0
            event.drops.clear()
        }
    }
}