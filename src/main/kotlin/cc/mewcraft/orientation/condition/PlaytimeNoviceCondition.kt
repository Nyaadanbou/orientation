package cc.mewcraft.orientation.condition

import cc.mewcraft.playtime.data.PlaytimeViewer
import java.util.*

class PlaytimeNoviceCondition(
    private val invalidPlaytime: Long
) : NoviceCondition {
    override suspend fun test(uniqueId: UUID): Boolean {
        val playtime = PlaytimeViewer.viewPlaytime(uniqueId) ?: return true
        return playtime.playTime <= invalidPlaytime
    }
}