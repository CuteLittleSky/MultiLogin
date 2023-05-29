package moe.caa.multilogin.core.database.table

import org.jetbrains.exposed.sql.Table

class HasWhitelistTableV1(tableName: String) : Table(tableName) {
    val onlineUUID = uuid("online_uuid")
    val serviceId = integer("service_id")

    override val primaryKey = PrimaryKey(onlineUUID, serviceId)
}
