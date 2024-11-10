@file:Suppress("UnstableApiUsage")

package cc.mewcraft.orientation

import cc.mewcraft.orientation.command.NoviceCommandManager
import cc.mewcraft.orientation.event.OrientationReloadEvent
import cc.mewcraft.orientation.locale.TranslationManager
import cc.mewcraft.orientation.novice.NoviceManager
import cc.mewcraft.orientation.protect.ProtectListener
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import org.bukkit.event.HandlerList
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.PaperCommandManager

internal val plugin: OrientationPlugin by lazy { OrientationPlugin.instance ?: throw IllegalStateException("Plugin not initialized yet") }

internal class OrientationPlugin: SuspendingJavaPlugin() {
    companion object {
        var instance: OrientationPlugin? = null
    }

    private lateinit var commandManager: NoviceCommandManager
    private var orientation: Orientation? = null

    val translationManager: TranslationManager by lazy { TranslationManager() }
    val noviceManager: NoviceManager by lazy { NoviceManager() }

    override suspend fun onEnableAsync() {
        instance = this
        val commandManager = PaperCommandManager.builder()
            .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
            .buildOnEnable(this)
        this.commandManager = NoviceCommandManager(commandManager).also { it.init() }

        noviceManager.onLoad()
        reload()

        server.pluginManager.registerSuspendingEvents(ProtectListener, this)

        orientation = OrientationImpl(this)
        OrientationProvider.register(orientation!!)
    }

    override suspend fun onDisableAsync() {
        instance = null
        orientation?.let { OrientationProvider.unregister() }
        noviceManager.onUnload()
        HandlerList.unregisterAll(this)
    }

    internal fun reload() {
        reloadConfig()
        saveDefaultConfig()
        translationManager.reload()

        OrientationReloadEvent().callEvent()
    }
}

