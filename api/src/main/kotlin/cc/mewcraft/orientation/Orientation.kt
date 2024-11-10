package cc.mewcraft.orientation

import cc.mewcraft.orientation.novice.Novice
import java.util.UUID

interface Orientation {
    /**
     * 获得一个新手.
     */
    fun getNovice(uniqueId: UUID): Novice
}