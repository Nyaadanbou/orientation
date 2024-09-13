package cc.mewcraft.newbieprotect.extension

import cc.mewcraft.newbieprotect.newbie.Newbie
import cc.mewcraft.newbieprotect.plugin
import org.bukkit.entity.Player

fun Player.toNewbie(): Newbie? {
    return plugin.newbieManager.getNewbie(this.uniqueId)
}