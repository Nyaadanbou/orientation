package cc.mewcraft.orientation.novice

import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.*

data class NoviceImpl(
    override val uniqueId: UUID,
    override val protects: ProtectGroup
) : Novice