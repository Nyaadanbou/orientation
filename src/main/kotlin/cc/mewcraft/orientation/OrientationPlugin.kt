package cc.mewcraft.orientation

import cc.mewcraft.orientation.novice.NoviceManager
import cc.mewcraft.orientation.protect.ProtectListener
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import org.bukkit.event.HandlerList

internal class OrientationPlugin : SuspendingJavaPlugin() {
    companion object {
        var INSTANCE: OrientationPlugin? = null
    }

    val noviceManager: NoviceManager = NoviceManager()

    override suspend fun onEnableAsync() {
        INSTANCE = this
        noviceManager.onLoad()
        server.pluginManager.registerSuspendingEvents(ProtectListener, this)
    }

    override suspend fun onDisableAsync() {
        INSTANCE = null
        noviceManager.onUnload()
        HandlerList.unregisterAll(this)
    }
}

internal val plugin: OrientationPlugin
    get() = OrientationPlugin.INSTANCE ?: throw IllegalStateException("Plugin not enabled")
