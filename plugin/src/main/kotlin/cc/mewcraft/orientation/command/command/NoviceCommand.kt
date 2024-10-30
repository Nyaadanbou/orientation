@file:Suppress("UnstableApiUsage")

package cc.mewcraft.orientation.command.command

import cc.mewcraft.orientation.command.CommandConstants
import cc.mewcraft.orientation.command.CommandPermissions
import cc.mewcraft.orientation.command.buildAndAdd
import cc.mewcraft.orientation.command.suspendingHandler
import cc.mewcraft.orientation.plugin
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.identity.Identity
import org.bukkit.command.CommandSender
import org.incendo.cloud.Command
import org.incendo.cloud.CommandFactory
import org.incendo.cloud.CommandManager
import org.incendo.cloud.bukkit.parser.PlayerParser
import org.incendo.cloud.description.Description
import org.incendo.cloud.kotlin.extension.commandBuilder
import kotlin.jvm.optionals.getOrNull

object NoviceCommand : CommandFactory<CommandSourceStack> {
    private const val CHECK_LITERAL = "check"
    private const val RESET_LITERAL = "reset"

    override fun createCommands(commandManager: CommandManager<CommandSourceStack>): List<Command<out CommandSourceStack>> {
        return buildList {
            // /<root> check <player>
            commandManager.commandBuilder(
                name = CommandConstants.ROOT_COMMAND,
                description = Description.of("Check your novice status"),
            ) {
                permission(CommandPermissions.NOVICE)
                literal(CHECK_LITERAL)
                optional("target", PlayerParser.playerParser())
                suspendingHandler { context ->
                    val sender = context.sender().sender
                    val target = context.optional<CommandSender>("target").orElse(sender)
                    val uniqueId = target.pointers().get(Identity.UUID).getOrNull()
                    if (uniqueId == null) {
                        sender.sendMessage("This player not found")
                        return@suspendingHandler
                    }
                    val novice = plugin.noviceManager.getNewbie(uniqueId)

                    sender.sendPlainMessage("Player ${target.name} is a novice - Time left: ${novice.timeLeft()}")
                }
            }.buildAndAdd(this)

            // /<root> reset <player>
            commandManager.commandBuilder(
                name = CommandConstants.ROOT_COMMAND,
                description = Description.of("Reset a player's novice status"),
            ) {
                permission(CommandPermissions.NOVICE)
                literal(RESET_LITERAL)
                optional("target", PlayerParser.playerParser())
                suspendingHandler { context ->
                    val sender = context.sender().sender
                    val target = context.optional<CommandSender>("target").orElse(sender)
                    val uniqueId = target.pointers().get(Identity.UUID).getOrNull()
                    if (uniqueId == null) {
                        sender.sendMessage("This player not found")
                        return@suspendingHandler
                    }

                    val novice = plugin.noviceManager.getNewbie(uniqueId)
                    novice.reset()
                    sender.sendMessage("Player ${target.name}'s novice status has been reset")
                }
            }.buildAndAdd(this)
        }
    }
}