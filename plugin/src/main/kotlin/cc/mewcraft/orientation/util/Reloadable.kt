package cc.mewcraft.orientation.util

import cc.mewcraft.orientation.event.OrientationReloadEvent
import cc.mewcraft.orientation.plugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal fun <T> reloadable(loader: () -> T) = Reloadable(loader)

internal class Reloadable<T>(private val loader: () -> T) : ReadOnlyProperty<Any?, T> {
    private var value: T? = null

    init {
        plugin.server.pluginManager.registerSuspendingEvents(ReloadListener(), plugin)
    }

    fun get(): T {
        val value = this.value
        if (value == null) {
            val createdValue = loader()
            this.value = createdValue
            return createdValue
        }
        return value
    }

    private fun reload() {
        value = null
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return get()
    }

    private inner class ReloadListener : Listener {
        @EventHandler
        fun onReload(event: OrientationReloadEvent) {
            reload()
        }
    }
}