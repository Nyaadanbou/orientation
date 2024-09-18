package cc.mewcraft.orientation.newbie

import cc.mewcraft.orientation.protect.ProtectGroup
import java.util.*

data class NewbieImpl(
    override val uniqueId: UUID,
    override val protects: ProtectGroup
) : Newbie