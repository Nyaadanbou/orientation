package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.protect.Protect
import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.*

object NoviceFactory {
    fun createNewbie(uniqueId: UUID): Novice {
        return NoviceImpl(uniqueId, ProtectGroup(Protect.KEEP_INV))
    }
}