package moe.caa.multilogin.core.config

import moe.caa.multilogin.core.createDirectoryIfNotExists
import moe.caa.multilogin.core.deleteAll
import moe.caa.multilogin.core.main.MultiCore
import moe.caa.multilogin.core.readConfigurationNode
import moe.caa.multilogin.core.subFile
import moe.caa.multilogin.logger.LoggerProvider
import moe.caa.multilogin.logger.bridges.DebugLoggerBridge
import org.spongepowered.configurate.ConfigurationNode
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.*
import java.util.jar.JarFile


/**
 * 插件配置阅读器
 */
class PluginConfig (
    multiCore: MultiCore
){
    private val configFolder = multiCore.plugin.configFolder
    private val serviceFolder = configFolder.subFile("services")
    private val exampleFolder = configFolder.subFile("examples")

    private lateinit var rootNode: ConfigurationNode

    fun reload(){
        serviceFolder.createDirectoryIfNotExists()
        exampleFolder.deleteAll()

        saveResourceDir(exampleFolder, "examples", true)
        rootNode = configFolder.subFile("config.yml").readConfigurationNode()

        val serviceConfigs: ArrayList<File> = ArrayList()
        Files.list(serviceFolder.toPath()).use { stream ->
            stream.filter { it.toFile().name.endsWith(".yml", ignoreCase = true) }.forEach {
                serviceConfigs.add(it.toFile())
            }
        }


    }

    fun initDebugMode(){
        if (rootNode.node("debug").getBoolean(false)) {
            DebugLoggerBridge.startDebugMode()
        } else {
            DebugLoggerBridge.cancelDebugMode()
        }
    }

    fun saveResourceDir(directory: File, path: String, cover: Boolean) {
        directory.createDirectoryIfNotExists()
        JarFile(File(javaClass.protectionDomain.codeSource.location.toURI())).use { jarFile ->
            jarFile.stream()
                .filter { it.realName.startsWith(path) }
                .filter { it.realName != "$path/" }
                .forEach {
                    val realName: String = it.realName
                    val fileName = realName.substring(path.length)
                    saveResource(cover, directory, realName, fileName)
                }
        }
    }

    private fun saveResource(cover: Boolean, file: File, realName: String, fileName: String) {
        val subFile = file.subFile(fileName)
        val exists = subFile.exists()
        if (exists && !cover) {
            return
        } else {
            if (!exists) Files.createFile(subFile.toPath())
        }
        Objects.requireNonNull(javaClass.getResourceAsStream("/$realName")).use { input ->
            FileOutputStream(subFile).use {
                it.write(input.readAllBytes())
            }
        }
        if (!exists) {
            LoggerProvider.getLogger().info("Extract: $realName")
        } else {
            LoggerProvider.getLogger().info("Cover: $realName")
        }
    }
}