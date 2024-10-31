package cc.mewcraft.orientation.command

import org.incendo.cloud.permission.Permission

object CommandPermissions {
    val RELOAD = Permission.of("orientation.command.reload")
    val NOVICE_CHECK = Permission.of("orientation.command.novice.check")
    val NOVICE_RESET = Permission.of("orientation.command.novice.reset")
}