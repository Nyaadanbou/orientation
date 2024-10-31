package cc.mewcraft.orientation.config

import cc.mewcraft.orientation.plugin
import cc.mewcraft.orientation.util.reloadable
import java.util.concurrent.TimeUnit

object NewbieConfig {
    val noviceEffectDuration: Long by reloadable { plugin.config.getLong("novice.effect_duration", TimeUnit.DAYS.toMillis(1)) }
}