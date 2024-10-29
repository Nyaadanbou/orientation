package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.config.NewbieConfig
import cc.mewcraft.orientation.protect.ProtectGroup
import cc.mewcraft.playtime.PlaytimeProvider
import java.util.*

data class NoviceImpl(
    override val uniqueId: UUID,
    override val protects: ProtectGroup,
) : Novice {
    private val playtimePlugin = PlaytimeProvider.get()
    override suspend fun isExpired(): Boolean {
        return timeLeft() <= 0
    }

    override suspend fun timeLeft(): Long {
        val playtime = playtimePlugin.getPlaytime(uniqueId)
        return NewbieConfig.noviceEffectDuration - (playtime?.playTime ?: 0)
    }
}