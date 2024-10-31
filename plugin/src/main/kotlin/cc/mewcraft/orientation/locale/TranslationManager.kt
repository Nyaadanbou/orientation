package cc.mewcraft.orientation.locale

import cc.mewcraft.orientation.config.ConfigManager
import cc.mewcraft.orientation.plugin
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.translation.Translator
import org.bukkit.configuration.file.FileConfiguration
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

class TranslationManager {
    private val miniMessage: MiniMessage = MiniMessage.miniMessage()
    private val installed: MutableSet<Locale> = ConcurrentHashMap.newKeySet()
    private var registry: MiniMessageTranslationRegistry? = null
    private val translationsDirectory: Path = plugin.dataPath.resolve("translations")

    fun reload() {
        // remove any previous registry
        if (this.registry != null) {
            MiniMessageTranslator.translator().removeSource(registry!!)
            installed.clear()
        }

        for (lang in locales) {
            ConfigManager.loadConfig("translations/$lang.yml")
        }

        this.registry = MiniMessageTranslationRegistry.create(Key.key("orientation", "main"), miniMessage)
        registry!!.defaultLocale(DEFAULT_LOCALE)
        this.loadFromFileSystem(this.translationsDirectory, false)
        MiniMessageTranslator.translator().addSource(registry!!)
    }

    fun loadFromFileSystem(directory: Path, suppressDuplicatesError: Boolean) {
        var translationFiles: List<Path>
        try {
            Files.list(directory).use { stream ->
                translationFiles = stream.filter { path: Path -> isTranslationFile(path) }.collect(Collectors.toList())
            }
        } catch (e: IOException) {
            translationFiles = emptyList()
        }

        if (translationFiles.isEmpty()) {
            return
        }

        val loaded: MutableMap<Locale, Map<String, String>> = HashMap()
        for (translationFile in translationFiles) {
            try {
                val result = loadTranslationFile(translationFile)
                loaded[result.first] = result.second
            } catch (e: Exception) {
                if (!suppressDuplicatesError || !isAdventureDuplicatesException(e)) {
                    plugin.componentLogger.warn("Error loading locale file: " + translationFile.fileName, e)
                }
            }
        }

        // try registering the locale without a country code - if we don't already have a registration for that
        loaded.forEach { (locale: Locale, bundle: Map<String, String>) ->
            val localeWithoutCountry = Locale.of(locale.language)
            if (locale != localeWithoutCountry && localeWithoutCountry != DEFAULT_LOCALE && installed.add(localeWithoutCountry)) {
                try {
                    registry!!.registerAll(localeWithoutCountry, bundle)
                } catch (e: IllegalArgumentException) {
                    // ignore
                }
            }
        }
    }

    private fun loadTranslationFile(translationFile: Path): Pair<Locale, Map<String, String>> {
        val fileName = translationFile.fileName.toString()
        val localeString = fileName.substring(0, fileName.length - ".yml".length)
        val locale = parseLocale(localeString)

        checkNotNull(locale) { "Unknown locale '$localeString' - unable to register." }

        val bundle: MutableMap<String, String> = HashMap()
        val document: FileConfiguration = ConfigManager.loadConfig("translations" + "\\" + translationFile.fileName) ?: return Pair(locale, bundle)
        val map: MutableMap<String, Any> = document.getValues(true)
        for ((key, value) in map) {
            if (value is List<*>) {
                @Suppress("UNCHECKED_CAST") val strList = value as List<String>
                val stringJoiner = StringJoiner("<reset><newline>")
                for (str in strList) {
                    stringJoiner.add(str)
                }
                bundle[key] = stringJoiner.toString()
            } else if (value is String) {
                bundle[key] = value
            }
        }

        registry!!.registerAll(locale, bundle)
        installed.add(locale)

        return Pair(locale, bundle)
    }

    companion object {
        val DEFAULT_LOCALE: Locale = Locale.SIMPLIFIED_CHINESE
        private val locales = listOf("zh_cn")

        @JvmOverloads
        fun render(component: Component, locale: Locale? = null): Component {
            var locale0 = locale
            if (locale0 == null) {
                locale0 = Locale.getDefault()
                if (locale0 == null) {
                    locale0 = DEFAULT_LOCALE
                }
            }
            return MiniMessageTranslator.render(component, locale0)
        }

        fun isTranslationFile(path: Path): Boolean {
            return path.fileName.toString().endsWith(".yml")
        }

        private fun isAdventureDuplicatesException(e: Exception): Boolean {
            return e is IllegalArgumentException && (e.message!!.startsWith("Invalid key") || e.message!!.startsWith("Translation already exists"))
        }

        fun parseLocale(locale: String?): Locale? {
            return if (locale == null) null else Translator.parseLocale(locale)
        }
    }
}