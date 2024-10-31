package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.config.NewbieConfig
import cc.mewcraft.orientation.locale.MessageConstants
import cc.mewcraft.orientation.plugin
import cc.mewcraft.orientation.util.formatDuration
import cc.mewcraft.orientation.util.render
import net.kyori.adventure.bossbar.BossBar
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object NoviceDisplay {
    private val bossBars = ConcurrentHashMap<UUID, BossBar>()

    fun bossBar(uniqueId: UUID, timeLeftMillis: Long) {
        val player = plugin.server.getPlayer(uniqueId) ?: return
        if (timeLeftMillis <= 0) {
            bossBars.remove(uniqueId)?.let { player.hideBossBar(it) }
            return
        }

        val bossBarMessage = MessageConstants.BOSSBAR_FORMAT.arguments(formatDuration(timeLeftMillis).render(player.locale())).build().render(player.locale())

        val oldBossBar = bossBars[uniqueId]
        if (oldBossBar != null) {
            oldBossBar.name(bossBarMessage)
            oldBossBar.progress(timeLeftMillis.toFloat() / NewbieConfig.noviceEffectDuration)
            return
        }

        val newBossBar = BossBar.bossBar(
            bossBarMessage,
            timeLeftMillis.toFloat() / NewbieConfig.noviceEffectDuration,
            BossBar.Color.GREEN,
            BossBar.Overlay.PROGRESS
        )

        bossBars[uniqueId] = newBossBar

        player.showBossBar(newBossBar)
    }
}