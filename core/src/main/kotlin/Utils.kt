import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import kotlin.experimental.and


fun UUID.toBytes(): ByteArray {
    val uuidBytes = ByteArray(16)
    ByteBuffer.wrap(uuidBytes).order(ByteOrder.BIG_ENDIAN).putLong(this.mostSignificantBits)
        .putLong(this.leastSignificantBits)
    return uuidBytes
}

fun ByteArray.toUUID(): UUID {
    // TODO: 有问题的
    var i = 0
    var msl: Long = 0
    while (i < 8) {
        msl = msl shl 8 or (this[i] and 0xFF.toByte()).toLong()
        i++
    }
    var lsl: Long = 0
    while (i < 16) {
        lsl = lsl shl 8 or (this[i] and 0xFF.toByte()).toLong()
        i++
    }
    return UUID(msl, lsl)
}