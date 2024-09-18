package cc.mewcraft.orientation.condition

import java.util.*

/**
 * 判断是否符合新手保护条件
 */
interface NewbieCondition {
    /**
     * 判断是否符合新手保护条件.
     *
     * @param uniqueId 玩家 uuid.
     * @return 是否符合新手保护条件.
     */
    suspend fun test(uniqueId: UUID): Boolean
}