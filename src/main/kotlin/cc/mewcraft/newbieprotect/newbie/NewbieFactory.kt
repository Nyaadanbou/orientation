package cc.mewcraft.newbieprotect.newbie

import cc.mewcraft.newbieprotect.config.NewbieConfig
import cc.mewcraft.newbieprotect.protect.KeepInvProtect
import cc.mewcraft.newbieprotect.protect.ProtectGroup
import java.util.UUID

object NewbieFactory {
    suspend fun createNewbie(uniqueId: UUID): Newbie? {
        if (NewbieConfig.newbieConditions.any { !it.test(uniqueId) })
            return null
        return NewbieImpl(uniqueId, ProtectGroup.create(KeepInvProtect::class.java))
    }
}