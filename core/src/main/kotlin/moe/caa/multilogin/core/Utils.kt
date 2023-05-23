package moe.caa.multilogin.core

import org.spongepowered.configurate.ConfigurationNode
import java.lang.reflect.AccessibleObject
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*


fun UUID.toBytes(): ByteArray {
    val uuidBytes = ByteArray(16)
    ByteBuffer.wrap(uuidBytes).order(ByteOrder.BIG_ENDIAN).putLong(this.mostSignificantBits)
        .putLong(this.leastSignificantBits)
    return uuidBytes
}

fun ByteArray.toUUID(): UUID {
    var i = 0
    var msl: Long = 0
    while (i < 8) {
        msl = (msl shl 8) or (this[i].toLong() and 0xFF)
        i++
    }
    var lsl: Long = 0
    while (i < 16) {
        lsl = (lsl shl 8) or (this[i].toLong() and 0xFF)
        i++
    }
    return UUID(msl, lsl)
}

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

fun <T : AccessibleObject> T.handleAccess(): T {
    this.isAccessible = true
    return this
}