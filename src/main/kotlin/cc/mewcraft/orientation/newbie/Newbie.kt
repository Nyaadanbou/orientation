package cc.mewcraft.orientation.newbie

import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.*

/**
 * 代表一个新手.
 */
interface Newbie {
    val uniqueId: UUID

    /**
     * 该新手拥有的保护.
     */
    val protects: ProtectGroup
}