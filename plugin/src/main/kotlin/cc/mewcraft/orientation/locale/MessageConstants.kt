package cc.mewcraft.orientation.locale

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent

object MessageConstants {
    val RELOAD_SUCCESS: TranslatableComponent.Builder = Component.translatable().key("commands.reload.success")
    val NOVICE_NOT_FOUND_RESULT: TranslatableComponent.Builder = Component.translatable().key("commands.error.novice.not_found")
    val NOVICE_CHECK_SUCCESS_RESULT: TranslatableComponent.Builder = Component.translatable().key("commands.success.check.novice.result")
    val NOVICE_CHECK_EXPIRED_RESULT: TranslatableComponent.Builder = Component.translatable().key("commands.error.check.novice.expired")
    val NOVICE_RESET_SUCCESS_RESULT: TranslatableComponent.Builder = Component.translatable().key("commands.success.reset.novice.result")
}