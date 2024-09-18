package cc.mewcraft.orientation.config

import cc.mewcraft.orientation.condition.NewbieCondition
import cc.mewcraft.orientation.condition.PlaytimeNewbieCondition

object NewbieConfig {
    val newbieConditions: Array<NewbieCondition> = arrayOf(
        PlaytimeNewbieCondition(20)
    )
}