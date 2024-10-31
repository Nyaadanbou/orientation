package cc.mewcraft.orientation.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class OrientationReloadEvent : Event() {
    override fun getHandlers() = HANDLERS

    companion object {
        @JvmStatic
        val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS
    }
}