package cc.mewcraft.orientation.extension

import cc.mewcraft.orientation.newbie.Newbie
import cc.mewcraft.orientation.plugin
import org.bukkit.entity.Player

fun Player.toNewbie(): Newbie? {
    return plugin.newbieManager.getNewbie(this.uniqueId)
}