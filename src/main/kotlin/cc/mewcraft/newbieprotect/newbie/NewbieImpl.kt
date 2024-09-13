package cc.mewcraft.newbieprotect.newbie

import cc.mewcraft.newbieprotect.protect.ProtectGroup
import java.util.*

data class NewbieImpl(
    override val uniqueId: UUID,
    override val protects: ProtectGroup
) : Newbie