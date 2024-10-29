@file:Suppress("UnstableApiUsage")

package cc.mewcraft.orientation.command

import cc.mewcraft.orientation.command.command.NoviceCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.incendo.cloud.paper.PaperCommandManager

class NoviceCommandManager(
    private val manager: PaperCommandManager<CommandSourceStack>,
) {
    fun init() {
        with(manager) {
            command(NoviceCommand)
        }
    }
}