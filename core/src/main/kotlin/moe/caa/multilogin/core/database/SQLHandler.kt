package moe.caa.multilogin.core.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import moe.caa.multilogin.core.database.table.*
import moe.caa.multilogin.core.toProperties
import moe.caa.multilogin.logger.LoggerProvider
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.spongepowered.configurate.ConfigurationNode

class SQLHandler {
    lateinit var cacheWhitelistTableV1: CacheWhitelistTableV1
    lateinit var hasWhitelistTableV1: HasWhitelistTableV1
    lateinit var inGameProfile: InGameProfileTableV3
    lateinit var skinRestorer: SkinRestoredCacheTableV2
    lateinit var userData: UserDataTableV4

    private lateinit var database: Database
    private lateinit var dataSource: HikariDataSource


    fun init(node: ConfigurationNode) {
        val tablePrefix = node.node("tablePrefix").getString("multilogin") + "_"
        val backend: SqlBackend = node.node("backend").get(SqlBackend::class.java, SqlBackend.H2)

        val hikariConfig = HikariConfig(node.node("properties").toProperties())
        dataSource = HikariDataSource(hikariConfig)

        database = Database.connect(dataSource)
        userData = UserDataTableV4("${tablePrefix}user_data_v3")
        inGameProfile = InGameProfileTableV3("${tablePrefix}in_game_profile_v3")
        skinRestorer = SkinRestoredCacheTableV2("${tablePrefix}skin_restored_cache_v2")
        cacheWhitelistTableV1 = CacheWhitelistTableV1("${tablePrefix}cache_whitelist_v1")
        hasWhitelistTableV1 = HasWhitelistTableV1("${tablePrefix}has_whitelist_v1")

        TransactionManager.defaultDatabase = database

        transaction {
            SchemaUtils.create(userData, inGameProfile, skinRestorer, cacheWhitelistTableV1, hasWhitelistTableV1)
        }

        LoggerProvider.getLogger().info("Connected to $backend.")
    }

    fun close() {
        if (::dataSource.isInitialized) {
            dataSource.close()
        }
    }
}