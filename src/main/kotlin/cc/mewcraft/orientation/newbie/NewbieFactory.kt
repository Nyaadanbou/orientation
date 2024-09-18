package cc.mewcraft.orientation.newbie

import cc.mewcraft.orientation.config.NewbieConfig
import cc.mewcraft.orientation.protect.KeepInvProtect
import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.UUID

object NewbieFactory {
    suspend fun createNewbie(uniqueId: UUID): Newbie? {
        if (NewbieConfig.newbieConditions.any { !it.test(uniqueId) })
            return null
        return NewbieImpl(uniqueId, ProtectGroup.create(KeepInvProtect::class.java))
    }
}