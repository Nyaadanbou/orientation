package cc.mewcraft.orientation.util

import net.kyori.adventure.text.Component

internal fun formatDuration(durationMills: Long): Component {
    val hours = durationMills / 3_600_000
    val minutes = (durationMills % 3_600_000) / 60_000
    val seconds = (durationMills % 60_000) / 1_000

    return when {
        hours > 0 && minutes > 0 -> Component.translatable("display.time.hours_minutes")
            .arguments(Component.text(hours), Component.text(minutes))
        hours > 0 && seconds > 0 -> Component.translatable("display.time.hours_seconds")
            .arguments(Component.text(hours), Component.text(seconds))
        hours > 0 -> Component.translatable("display.time.hours")
            .arguments(Component.text(hours))
        minutes > 0 && seconds > 0 -> Component.translatable("display.time.minutes_seconds")
            .arguments(Component.text(minutes), Component.text(seconds))
        minutes > 0 -> Component.translatable("display.time.minutes")
            .arguments(Component.text(minutes))
        else -> Component.translatable("display.time.seconds")
            .arguments(Component.text(seconds))
    }
}