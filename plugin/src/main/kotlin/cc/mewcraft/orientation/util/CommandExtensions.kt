package cc.mewcraft.orientation.util

import cc.mewcraft.orientation.locale.TranslationManager
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

internal inline fun CommandSender.sendRenderedMessage(component: (Locale?) -> Component) {
    if (this is Player) {
        val locale = locale()
        val message = TranslationManager.render(component(locale), locale)
        this.sendMessage(message)
    } else {
        this.sendMessage(TranslationManager.render(component(null), null))
    }
}