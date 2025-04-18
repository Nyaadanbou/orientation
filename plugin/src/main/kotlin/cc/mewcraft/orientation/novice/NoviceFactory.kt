package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.protect.Protect
import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.*

object NoviceFactory {
    suspend fun createNewbie(uniqueId: UUID): Novice? {
        return NoviceImpl(uniqueId, ProtectGroup.create(Protect.KEEP_INV)).takeIf { !it.isExpired() }
    }
}