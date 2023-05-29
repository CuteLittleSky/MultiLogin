package moe.caa.multilogin.core.database.table

import org.jetbrains.exposed.sql.Table

class UserDataTableV4(tableName: String) : Table(tableName) {
    val onlineUUID = uuid("online_uuid")
    val serviceId = integer("service_id")
    val onlineName = varchar("online_name", 64)
    val inGameProfileUUID = uuid("in_game_profile_uuid")

    override val primaryKey = PrimaryKey(onlineUUID, serviceId)
}