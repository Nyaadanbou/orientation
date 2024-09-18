package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.config.NewbieConfig
import cc.mewcraft.orientation.protect.KeepInvProtect
import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.UUID

object NoviceFactory {
    suspend fun createNewbie(uniqueId: UUID): Novice? {
        if (NewbieConfig.noviceConditions.any { !it.test(uniqueId) })
            return null
        return NoviceImpl(uniqueId, ProtectGroup.create(KeepInvProtect::class.java))
    }
}