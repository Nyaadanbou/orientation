package cc.mewcraft.orientation.extension

import cc.mewcraft.orientation.novice.Novice
import cc.mewcraft.orientation.plugin
import org.bukkit.entity.Player

fun Player.toNewbie(): Novice? {
    return plugin.noviceManager.getNovice(this.uniqueId)
}