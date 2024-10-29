package cc.mewcraft.orientation.config

import cc.mewcraft.orientation.condition.NoviceCondition
import cc.mewcraft.orientation.condition.PlaytimeNoviceCondition
import java.util.concurrent.TimeUnit

object NewbieConfig {
    val noviceConditions: List<NoviceCondition> = listOf(
        PlaytimeNoviceCondition(24, TimeUnit.HOURS)
    )
}