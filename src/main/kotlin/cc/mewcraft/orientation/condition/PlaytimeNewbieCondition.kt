package cc.mewcraft.orientation.condition

import cc.mewcraft.playtime.data.PlaytimeViewer
import java.util.*

class PlaytimeNewbieCondition(
    private val invalidPlaytime: Long
) : NewbieCondition {
    override suspend fun test(uniqueId: UUID): Boolean {
        val playtime = PlaytimeViewer.viewPlaytime(uniqueId) ?: return true
        return playtime.playTime <= invalidPlaytime
    }
}