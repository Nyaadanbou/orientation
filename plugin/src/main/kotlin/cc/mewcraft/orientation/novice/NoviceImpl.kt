package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.condition.NoviceCondition
import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.*

data class NoviceImpl(
    override val uniqueId: UUID,
    override val protects: ProtectGroup,
    override val conditions: Collection<NoviceCondition>
) : Novice {
    override suspend fun isExpired(): Boolean {
        return conditions.any { !it.test(this.uniqueId) }
    }
}