package cc.mewcraft.orientation.config

import cc.mewcraft.orientation.condition.NoviceCondition
import cc.mewcraft.orientation.condition.PlaytimeNoviceCondition

object NewbieConfig {
    val noviceConditions: Array<NoviceCondition> = arrayOf(
        PlaytimeNoviceCondition(20)
    )
}