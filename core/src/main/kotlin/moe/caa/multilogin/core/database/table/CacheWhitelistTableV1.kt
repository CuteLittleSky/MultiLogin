package moe.caa.multilogin.core.database.table

import moe.caa.multilogin.core.toBytes
import moe.caa.multilogin.core.toUUID
import org.ktorm.schema.Table
import org.ktorm.schema.bytes
import org.ktorm.schema.int

class CacheWhitelistTableV1(tableName: String) : Table<Nothing>(tableName) {
    val data = bytes("data").primaryKey()
        .transform({ it.toUUID() }, { it.toBytes() })
    val serviceId = int("service_id").primaryKey()
}
