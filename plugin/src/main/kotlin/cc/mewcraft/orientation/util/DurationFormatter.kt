/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package cc.mewcraft.orientation.util

import cc.mewcraft.orientation.locale.TranslationManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * Formats durations to a readable form
 */
class DurationFormatter @JvmOverloads constructor(private val concise: Boolean, private val accuracy: Int = Int.MAX_VALUE) {
    /**
     * Formats `duration` as a string.
     *
     * @param duration the duration
     * @return the formatted string
     */
    fun formatString(duration: Duration): String {
        return PlainTextComponentSerializer.plainText().serialize(TranslationManager.render(format(duration)))
    }

    /**
     * Formats `duration` as a [Component].
     *
     * @param duration the duration
     * @return the formatted component
     */
    fun format(duration: Duration): Component {
        var seconds = duration.seconds
        val builder = Component.text()
        var outputSize = 0

        for (unit in UNITS) {
            val n = seconds / unit.duration.seconds
            if (n > 0) {
                seconds -= unit.duration.seconds * n
                if (outputSize != 0) {
                    builder.append(Component.space())
                }
                builder.append(formatPart(n, unit))
                outputSize++
            }
            if (seconds <= 0 || outputSize >= this.accuracy) {
                break
            }
        }

        if (outputSize == 0) {
            return formatPart(0, ChronoUnit.SECONDS)
        }
        return builder.build()
    }

    // Translation keys are in the format:
    //   display.duration.unit.years.plural={0} years
    //   display.duration.unit.years.singular={0} year
    //   display.duration.unit.years.short={0}y
    // ... and so on
    private fun formatPart(amount: Long, unit: ChronoUnit): TranslatableComponent {
        val format = if (this.concise) "short" else if (amount == 1L) "singular" else "plural"
        val translationKey = "display.duration.unit." + unit.name.lowercase() + "." + format
        return Component.translatable(translationKey, Component.text(amount))
    }

    companion object {
        val LONG: DurationFormatter = DurationFormatter(false)
        val CONCISE: DurationFormatter = DurationFormatter(true)
        val CONCISE_LOW_ACCURACY: DurationFormatter = DurationFormatter(true, 3)

        private val UNITS = arrayOf(
            ChronoUnit.YEARS,
            ChronoUnit.MONTHS,
            ChronoUnit.WEEKS,
            ChronoUnit.DAYS,
            ChronoUnit.HOURS,
            ChronoUnit.MINUTES,
            ChronoUnit.SECONDS
        )
    }
}