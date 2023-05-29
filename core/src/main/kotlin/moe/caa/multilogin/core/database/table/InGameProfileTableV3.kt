package moe.caa.multilogin.core.database.table

import org.jetbrains.exposed.sql.Table

class InGameProfileTableV3(tableName: String) : Table(tableName) {
    val inGameUUID = uuid("in_game_uuid")
    val currentUsernameLowerCase = varchar("current_username_lower_case", 64).uniqueIndex()
    val currentUsernameOriginal = varchar("current_username_original", 64)

    override val primaryKey = PrimaryKey(inGameUUID)
}