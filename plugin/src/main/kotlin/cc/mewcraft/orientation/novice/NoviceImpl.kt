package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.config.NewbieConfig
import cc.mewcraft.orientation.plugin
import cc.mewcraft.orientation.protect.ProtectGroup
import cc.mewcraft.orientation.util.AutoRefreshValue
import cc.mewcraft.playtime.PlaytimeProvider
import cc.mewcraft.playtime.data.PlaytimeData
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    override val maxTimeMillSeconds: Long
        get() = NewbieConfig.noviceEffectDuration

    private val timeLeftCache: AutoRefreshValue<Long> = AutoRefreshValue(1.toDuration(DurationUnit.MINUTES), Dispatchers.IO) {
        val playtime = playtimePlugin.getPlaytime(uniqueId)
        maxTimeMillSeconds - (playtime?.playTime ?: 0)
    }

    override fun addRefreshListener(listener: NoviceRefreshListener<Long>) {
        timeLeftCache.addRefreshListener(listener)
    }

    override suspend fun isExpired(): Boolean {
        return timeLeftMillSeconds() <= 0
    }

    override suspend fun timeLeftMillSeconds(): Long {
        return timeLeftCache.getValue()
            ?: timeLeftCache.refreshValue()
    }

    override fun reset() {
        plugin.scope.launch(plugin.asyncDispatcher) {
            playtimePlugin.setPlaytime(uniqueId, PlaytimeData())
            timeLeftCache.refreshValue()
        }
    }

    override fun refresh() {
        plugin.scope.launch(plugin.asyncDispatcher) {
            timeLeftCache.refreshValue()
        }
    }

    suspend fun destroy() {
        timeLeftCache.stopRefreshing()
    }
}