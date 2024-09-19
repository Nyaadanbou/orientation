package cc.mewcraft.orientation.condition

import cc.mewcraft.playtime.PlaytimeProvider
import java.util.*

class PlaytimeNoviceCondition(
    private val invalidPlaytime: Long
) : NoviceCondition {
    override suspend fun test(uniqueId: UUID): Boolean {
        val pt = PlaytimeProvider.get()
        val playtime = pt.getPlaytime(uniqueId) ?: return true
        return playtime.playTime <= invalidPlaytime
    }
}