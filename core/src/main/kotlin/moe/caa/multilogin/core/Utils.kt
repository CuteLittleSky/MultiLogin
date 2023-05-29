package moe.caa.multilogin.core

import org.spongepowered.configurate.ConfigurationNode
import java.lang.reflect.AccessibleObject
import java.util.*

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

fun ConfigurationNode.toProperties(): Properties {
    val properties = Properties()

    this.childrenMap().forEach { entry ->
        val key = entry.key.toString()
        val value = entry.value.raw()
        if (value is Map<*, *>) {
            value.toProperties().forEach { t, u ->
                properties["$key.$t"] = u
            }
        } else {
            properties[key] = value
        }
    }
    return properties
}

fun <T : AccessibleObject> T.openAccess(): T {
    this.isAccessible = true
    return this
}