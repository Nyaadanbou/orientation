package cc.mewcraft.orientation

import cc.mewcraft.orientation.novice.NoviceManager
import cc.mewcraft.orientation.protect.ProtectListener
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import org.bukkit.event.HandlerList

internal val plugin: OrientationPlugin
    get() = OrientationPlugin.instance ?: throw IllegalStateException("Plugin not initialized yet")

internal class OrientationPlugin : SuspendingJavaPlugin() {
    companion object {
        var instance: OrientationPlugin? = null
    }

    val noviceManager: NoviceManager by lazy { NoviceManager() }

    override suspend fun onEnableAsync() {
        instance = this
        noviceManager.onLoad()
        server.pluginManager.registerSuspendingEvents(ProtectListener, this)
    }

    override suspend fun onDisableAsync() {
        instance = null
        noviceManager.onUnload()
        HandlerList.unregisterAll(this)
    }
}

