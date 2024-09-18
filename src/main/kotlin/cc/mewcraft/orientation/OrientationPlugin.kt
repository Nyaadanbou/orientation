package cc.mewcraft.orientation

import cc.mewcraft.orientation.newbie.NewbieManager
import cc.mewcraft.orientation.protect.ProtectListener
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import org.bukkit.event.HandlerList

internal class OrientationPlugin : SuspendingJavaPlugin() {
    companion object {
        var INSTANCE: OrientationPlugin? = null
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

internal val plugin: OrientationPlugin
    get() = OrientationPlugin.INSTANCE ?: throw IllegalStateException("Plugin not enabled")
