package cc.mewcraft.orientation

import cc.mewcraft.orientation.novice.Novice
import java.util.*

internal class OrientationImpl(
    private val plugin: OrientationPlugin
) : Orientation {
    override fun getNovice(uniqueId: UUID): Novice {
        return plugin.noviceManager.getNovice(uniqueId)
    }
}