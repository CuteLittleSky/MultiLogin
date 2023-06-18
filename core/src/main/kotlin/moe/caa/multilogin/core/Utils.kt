package moe.caa.multilogin.core

import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File
import java.lang.reflect.AccessibleObject
import java.nio.file.Files
import java.util.*


// Map <=> Properties
fun Map<*, *>.toProperties(): Properties {
    val properties = Properties()

    for (entry in this.entries) {
        val key = entry.key.toString()
        val value = entry.value?.toString()
        if (value != null) {
            if (entry.value is Map<*, *>) {
                (entry.value as Map<*, *>).toProperties().forEach { t, u ->
                    properties["$key.$t"] = u
                }
            } else {
                properties.setProperty(key, value)
            }
        }
    }

    return properties
}

// ConfigurationNode <=> Properties
fun ConfigurationNode.toProperties(): Properties {
    val properties = Properties()

    this.childrenMap().forEach { entry ->
        val key = entry.key.toString()
        val value = entry.value.raw()
        if (value is Map<*, *>)
            value.toProperties().forEach { t, u ->
                properties["$key.$t"] = u
            }
        else properties[key] = value
    }
    return properties
}

// AccessibleObject
fun <T : AccessibleObject> T.openAccess(): T {
    this.let {
        isAccessible = true
    }
    return this
}

// File
fun File.createDirectoryIfNotExists(){
    if (!this.exists()) Files.createDirectories(this.toPath())
}

// 递归删除文件
fun File.deleteAll(){
    if (!this.exists()) return
    if(this.isDirectory) this.listFiles()?.forEach {it.deleteAll()}
    Files.delete(this.toPath())
}

fun File.readConfigurationNode(): CommentedConfigurationNode =
    YamlConfigurationLoader.builder().file(this).build().load()

fun File.subFile(name: String): File =
    File(this, name)
