package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.config.NewbieConfig
import cc.mewcraft.orientation.protect.ProtectGroup
import cc.mewcraft.orientation.util.AutoRefreshValue
import cc.mewcraft.playtime.PlaytimeProvider
import cc.mewcraft.playtime.data.PlaytimeData
import kotlinx.coroutines.Dispatchers
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class NoviceImpl(
    override val uniqueId: UUID,
    override val protects: ProtectGroup,
) : Novice {
    companion object {
        private val playtimePlugin = PlaytimeProvider.get()
    }

    private val timeLeftCache: AutoRefreshValue<Long> = AutoRefreshValue(1.toDuration(DurationUnit.MINUTES), Dispatchers.IO) {
        val playtime = playtimePlugin.getPlaytime(uniqueId)
        NewbieConfig.noviceEffectDuration - (playtime?.playTime ?: 0)
    }

    override suspend fun isExpired(): Boolean {
        return timeLeftMillSeconds() <= 0
    }

    override suspend fun timeLeftMillSeconds(): Long {
        return timeLeftCache.getValue()
            ?: timeLeftCache.refreshValue()
    }

    override suspend fun reset() {
        playtimePlugin.setPlaytime(uniqueId, PlaytimeData())
        timeLeftCache.refreshValue()
    }

    override suspend fun refresh() {
        timeLeftCache.refreshValue()
    }

    suspend fun destroy() {
        timeLeftCache.stopRefreshing()
    }
}