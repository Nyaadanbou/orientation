@file:Suppress("UnstableApiUsage")

package cc.mewcraft.orientation.command.command

import cc.mewcraft.orientation.command.CommandConstants
import cc.mewcraft.orientation.command.CommandPermissions
import cc.mewcraft.orientation.command.buildAndAdd
import cc.mewcraft.orientation.command.suspendingHandler
import cc.mewcraft.orientation.locale.MessageConstants
import cc.mewcraft.orientation.plugin
import cc.mewcraft.orientation.util.DurationFormatter
import cc.mewcraft.orientation.util.render
import cc.mewcraft.orientation.util.sendRenderedMessage
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.incendo.cloud.Command
import org.incendo.cloud.CommandFactory
import org.incendo.cloud.CommandManager
import org.incendo.cloud.bukkit.parser.PlayerParser
import org.incendo.cloud.description.Description
import org.incendo.cloud.kotlin.extension.commandBuilder
import java.time.Duration
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
                permission(CommandPermissions.NOVICE_CHECK)
                literal(CHECK_LITERAL)
                optional("target", PlayerParser.playerParser())
                suspendingHandler { context ->
                    val sender = context.sender().sender
                    val target = context.optional<CommandSender>("target").orElse(sender)
                    val uniqueId = target.pointers().get(Identity.UUID).getOrNull()
                    if (uniqueId == null) {
                        sender.sendRenderedMessage {
                            MessageConstants.NOVICE_NOT_FOUND_RESULT.arguments(Component.text(target.name)).build()
                        }
                        return@suspendingHandler
                    }
                    val novice = plugin.noviceManager.getNewbie(uniqueId)

                    if (novice.isExpired()) {
                        sender.sendRenderedMessage { locale ->
                            val timeLeft = novice.timeLeftMillSeconds() * -1
                            val timeMessage = DurationFormatter.CONCISE.format(Duration.ofMillis(timeLeft)).render(locale)
                            MessageConstants.NOVICE_CHECK_EXPIRED_RESULT.arguments(timeMessage).build()
                        }
                        return@suspendingHandler
                    }

                    sender.sendRenderedMessage { locale ->
                        val timeLeft = novice.timeLeftMillSeconds()
                        val timeMessage = DurationFormatter.CONCISE.format(Duration.ofMillis(timeLeft)).render(locale)
                        MessageConstants.NOVICE_CHECK_SUCCESS_RESULT.arguments(timeMessage).build()
                    }
                }
            }.buildAndAdd(this)

            // /<root> reset <player>
            commandManager.commandBuilder(
                name = CommandConstants.ROOT_COMMAND,
                description = Description.of("Reset a player's novice status"),
            ) {
                permission(CommandPermissions.NOVICE_RESET)
                literal(RESET_LITERAL)
                optional("target", PlayerParser.playerParser())
                suspendingHandler { context ->
                    val sender = context.sender().sender
                    val target = context.optional<CommandSender>("target").orElse(sender)
                    val uniqueId = target.pointers().get(Identity.UUID).getOrNull()
                    if (uniqueId == null) {
                        sender.sendRenderedMessage {
                            MessageConstants.NOVICE_NOT_FOUND_RESULT.arguments(Component.text(target.name)).build()
                        }
                        return@suspendingHandler
                    }

                    val novice = plugin.noviceManager.getNewbie(uniqueId)
                    novice.reset()
                    sender.sendRenderedMessage {
                        MessageConstants.NOVICE_RESET_SUCCESS_RESULT.arguments(Component.text(target.name)).build()
                    }
                }
            }.buildAndAdd(this)
        }
    }
}