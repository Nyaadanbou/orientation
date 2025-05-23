package cc.mewcraft.orientation.command

import cc.mewcraft.orientation.plugin
import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.scope
import kotlinx.coroutines.CoroutineScope
import org.bukkit.command.CommandSender
import org.incendo.cloud.Command
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.incendo.cloud.kotlin.coroutines.SuspendingExecutionHandler
import org.incendo.cloud.kotlin.coroutines.extension.suspendingHandler
import kotlin.coroutines.CoroutineContext

/**
 * Specify a suspending command execution handler.
 */
fun <C : Any> MutableCommandBuilder<C>.suspendingHandler(
    scope: CoroutineScope = plugin.scope, // use our own scope
    context: CoroutineContext = plugin.asyncDispatcher, // use our own dispatcher
    handler: SuspendingExecutionHandler<C>,
): MutableCommandBuilder<C> = mutate {
    it.suspendingHandler(scope, context, handler)
}

/**
 * Builds this command and adds it to the given [commands].
 *
 * @param C the sender type
 * @param commands the command list
 */
fun <C : Any> MutableCommandBuilder<C>.buildAndAdd(commands: MutableList<Command<out C>>) {
    commands += this.build()
}