package cc.mewcraft.newbieprotect.config

import cc.mewcraft.newbieprotect.condition.NewbieCondition
import cc.mewcraft.newbieprotect.condition.PlaytimeNewbieCondition

object NewbieConfig {
    val newbieConditions: Array<NewbieCondition> = arrayOf(
        PlaytimeNewbieCondition(20)
    )
}