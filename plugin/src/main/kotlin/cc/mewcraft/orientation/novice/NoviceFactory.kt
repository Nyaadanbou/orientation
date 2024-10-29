package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.config.NewbieConfig
import cc.mewcraft.orientation.protect.Protect
import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.UUID

object NoviceFactory {
    suspend fun createNewbie(uniqueId: UUID): Novice? {
        val noviceConditions = NewbieConfig.noviceConditions
        if (noviceConditions.any { !it.test(uniqueId) })
            // The player does not meet the conditions to be a newbie,
            return null
        return NoviceImpl(uniqueId, ProtectGroup.create(Protect.KEEP_INV), noviceConditions)
    }
}