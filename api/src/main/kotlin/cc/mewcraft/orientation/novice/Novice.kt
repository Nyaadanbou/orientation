package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.*

/**
 * 代表一个可能是新手的用户. 此用户不一定是一个新手, 他可能是一个已经过期的新手.
 *
 * 调用方应该使用 [isExpired] 方法来判断该用户是否是一个新手.
 *
 * 新手是一个玩家, 他在服务器上的时间不足一定时间.
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
     * 该新手的最大新手时间, 单位为毫秒.
     */
    val maxTimeMillSeconds: Long

    /**
     * 添加一个新手状态刷新监听器.
     */
    fun addRefreshListener(listener: NoviceRefreshListener<Long>)

    /**
     * 该新手是否已经过期.
     */
    suspend fun isExpired(): Boolean

    /**
     * 返回该新手还剩下多少时间成为一个非新手, 单位为毫秒.
     */
    suspend fun timeLeftMillSeconds(): Long

    /**
     * 重置该新手的新手状态.
     */
    fun reset()

    /**
     * 刷新该新手的状态.
     */
    fun refresh()
}