package moe.caa.multilogin.core.database.table

import org.ktorm.schema.Table
import org.ktorm.schema.bytes
import org.ktorm.schema.text
import org.ktorm.schema.varchar

class SkinRestoredCacheTableV2(tableName: String) : Table<Nothing>(tableName) {
    val currentSkinUrlSha256 = bytes("current_skin_url_sha256").primaryKey()
    val currentSkinModel = varchar("current_skin_model").primaryKey()
    val restorerValue = text("restorer_value")
    val restorerSignature = text("restorer_signature")
}