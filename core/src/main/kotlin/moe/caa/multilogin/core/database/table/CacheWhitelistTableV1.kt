package moe.caa.multilogin.core.database.table

import org.jetbrains.exposed.sql.Table

class CacheWhitelistTableV1(tableName: String) : Table(tableName) {
    val data = varchar("data", 64)
    val serviceId = integer("service_id")

    override val primaryKey = PrimaryKey(data, serviceId)
}
