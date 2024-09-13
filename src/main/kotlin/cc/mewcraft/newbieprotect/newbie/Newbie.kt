package cc.mewcraft.newbieprotect.newbie

import cc.mewcraft.newbieprotect.protect.ProtectGroup
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