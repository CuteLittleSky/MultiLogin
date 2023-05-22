package moe.caa.multilogin.core.database.table

import org.ktorm.schema.Table
import org.ktorm.schema.bytes
import org.ktorm.schema.varchar

class InGameProfileV3(tableName: String) : Table<Nothing>(tableName) {
    val inGameUUID = bytes("in_game_uuid").primaryKey()
    val currentUsernameLowerCase = varchar("current_username_lower_case")
    val currentUsernameOriginal = varchar("current_username_original")
}