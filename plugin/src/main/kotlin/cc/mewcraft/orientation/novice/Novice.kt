package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.condition.NoviceCondition
import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.*

/**
 * 代表一个新手.
 */
interface Novice {
    /**
     * 该新手的唯一标识符.
     */
    val uniqueId: UUID

    /**
     * 该新手拥有的保护.
     */
    val protects: ProtectGroup

    /**
     * 该新手的条件.
     */
    val conditions: Collection<NoviceCondition>

    suspend fun isExpired(): Boolean
}