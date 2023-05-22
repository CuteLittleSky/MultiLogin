package moe.caa.multilogin.core.database.table

import org.ktorm.schema.*
import toBytes
import toUUID

class UserDataV3(tableName: String) : Table<Nothing>(tableName) {
    val onlineUUID = bytes("online_uuid").primaryKey()
        .transform({ it.toUUID() }, { it.toBytes() })
    val serviceId = int("service_id").primaryKey()
    val onlineName = varchar("online_name")
    val inGameProfileUUID = bytes("in_game_profile_uuid").primaryKey()
        .transform({ it.toUUID() }, { it.toBytes() })
    val whitelist = boolean("whitelist")
}