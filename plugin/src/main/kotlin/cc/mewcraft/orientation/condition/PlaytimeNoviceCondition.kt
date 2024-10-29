package cc.mewcraft.orientation.condition

import cc.mewcraft.playtime.PlaytimeProvider
import java.util.*
import java.util.concurrent.TimeUnit

class PlaytimeNoviceCondition(
    /**
     * 当 invalidPlaytime 小于等于玩家的游戏时间时，玩家可以通过检测
     */
    invalidPlaytime: Long,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
) : NoviceCondition {
    private val invalidPlaytime: Long = timeUnit.toSeconds(invalidPlaytime)

    override suspend fun test(uniqueId: UUID): Boolean {
        val pt = PlaytimeProvider.get()
        val playtime = pt.getPlaytime(uniqueId) ?: return true
        return playtime.playTime <= invalidPlaytime
    }

    suspend fun getTimeLeft(uniqueId: UUID): Long {
        val pt = PlaytimeProvider.get()
        val playtime = pt.getPlaytime(uniqueId) ?: return 0
        return invalidPlaytime - playtime.playTime
    }
}