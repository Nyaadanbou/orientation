package cc.mewcraft.orientation.util

import cc.mewcraft.orientation.locale.TranslationManager
import net.kyori.adventure.text.Component
import java.util.Locale

internal fun Component.render(locale: Locale?): Component {
    return TranslationManager.render(this, locale)
}