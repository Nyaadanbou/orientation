package cc.mewcraft.orientation.config

import cc.mewcraft.orientation.plugin
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.outputStream

object ConfigManager {
    private fun resolveConfig(filePath0: String): Path {
        var filePath = filePath0
        require(filePath.isNotEmpty()) { "ResourcePath cannot be null or empty" }
        filePath = filePath.replace('\\', '/')
        val configFile: Path = plugin.dataPath.resolve(filePath)

        // if the config doesn't exist, create it based on the template in the resources dir
        if (!configFile.exists()) {
            configFile.parent.createDirectories()

            plugin.getResource(filePath).use {
                requireNotNull(it) { "The embedded resource '$filePath' cannot be found" }
                it.copyTo(configFile.outputStream())
            }
        }

        return configFile
    }

    fun loadConfig(filePath: String): FileConfiguration? {
        return try {
            val configFile = resolveConfig(filePath)
            plugin.logger.info("Loading config from $configFile")
            YamlConfiguration.loadConfiguration(configFile.toFile())
        } catch (e: Exception) {
            plugin.logger.severe("Failed to load config: $e")
            null
        }
    }
}