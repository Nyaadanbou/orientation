package cc.mewcraft.newbieprotect

import cc.mewcraft.newbieprotect.newbie.NewbieManager
import cc.mewcraft.newbieprotect.protect.ProtectListener
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import org.bukkit.event.HandlerList

internal class NewbieProtect : SuspendingJavaPlugin() {
    companion object {
        var INSTANCE: NewbieProtect? = null
    }

    val newbieManager: NewbieManager = NewbieManager()

    override suspend fun onEnableAsync() {
        INSTANCE = this
        newbieManager.onLoad()
        server.pluginManager.registerSuspendingEvents(ProtectListener, this)
    }

    override suspend fun onDisableAsync() {
        INSTANCE = null
        newbieManager.onUnload()
        HandlerList.unregisterAll(this)
    }
}

internal val plugin: NewbieProtect
    get() = NewbieProtect.INSTANCE ?: throw IllegalStateException("Plugin not enabled")
