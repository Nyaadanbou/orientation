@file:Suppress("UnstableApiUsage")

package cc.mewcraft.orientation.command.command

import cc.mewcraft.orientation.command.CommandConstants
import cc.mewcraft.orientation.command.CommandPermissions
import cc.mewcraft.orientation.command.buildAndAdd
import cc.mewcraft.orientation.command.suspendingHandler
import cc.mewcraft.orientation.locale.MessageConstants
import cc.mewcraft.orientation.plugin
import cc.mewcraft.orientation.util.sendRenderedMessage
import com.github.shynixn.mccoroutine.bukkit.scope
import io.papermc.paper.command.brigadier.CommandSourceStack
import kotlinx.coroutines.async
import net.kyori.adventure.text.Component
import org.incendo.cloud.Command
import org.incendo.cloud.CommandFactory
import org.incendo.cloud.CommandManager
import org.incendo.cloud.description.Description
import org.incendo.cloud.kotlin.extension.commandBuilder
import kotlin.system.measureTimeMillis

object OrientationCommand : CommandFactory<CommandSourceStack> {
    private const val RELOAD_LITERAL = "reload"

    override fun createCommands(commandManager: CommandManager<CommandSourceStack>): List<Command<out CommandSourceStack>> {
        return buildList {
            // /<root> reload
            commandManager.commandBuilder(
                name = CommandConstants.ROOT_COMMAND,
                description = Description.of("Check your novice status"),
            ) {
                permission(CommandPermissions.RELOAD)
                literal(RELOAD_LITERAL)
                suspendingHandler { context ->
                    val sender = context.sender().sender

                    val reloadTime = measureTimeMillis {
                        plugin.scope.async {
                            plugin.reload()
                        }.await()
                    }

                    sender.sendRenderedMessage { MessageConstants.RELOAD_SUCCESS.arguments(Component.text(reloadTime)).build() }
                }
            }.buildAndAdd(this)
        }
    }
}