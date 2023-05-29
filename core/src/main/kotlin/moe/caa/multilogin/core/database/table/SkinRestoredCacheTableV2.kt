package moe.caa.multilogin.core.database.table

import org.jetbrains.exposed.sql.Table

class SkinRestoredCacheTableV2(tableName: String) : Table(tableName) {
    val currentSkinUrlSha256 = binary("current_skin_url_sha256", 32)
    val currentSkinModel = varchar("current_skin_model", 16)
    val restorerValue = text("restorer_value")
    val restorerSignature = text("restorer_signature")

    override val primaryKey = PrimaryKey(currentSkinUrlSha256, currentSkinModel)
}