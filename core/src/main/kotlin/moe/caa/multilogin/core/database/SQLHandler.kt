package moe.caa.multilogin.core.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import moe.caa.multilogin.core.database.table.InGameProfileV3
import moe.caa.multilogin.core.database.table.SkinRestoredCacheV2
import moe.caa.multilogin.core.database.table.UserDataV3
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.spongepowered.configurate.ConfigurationNode
import java.util.*

class SQLHandler {
    lateinit var userData: UserDataV3
    lateinit var inGameProfile: InGameProfileV3
    lateinit var skinRestorer: SkinRestoredCacheV2
    lateinit var dataSource: HikariDataSource
    lateinit var database: Database

    fun init(configurationNode: ConfigurationNode) {
        val tablePrefix = "multilogin_"
        // todo 从外面传参
        val properties = Properties()
        properties["dataSource.user"] = "root"
        properties["dataSource.password"] = "111111"
        properties["jdbcUrl"] = "jdbc:mysql://localhost:3306/multilogin?useSSL=false"
        dataSource = HikariDataSource(HikariConfig(properties))

        database = Database.connect(dataSource)
        userData = UserDataV3("${tablePrefix}user_data_v3")
        inGameProfile = InGameProfileV3("${tablePrefix}in_game_profile_v3")
        skinRestorer = SkinRestoredCacheV2("${tablePrefix}skin_restored_cache_v2")

        val query =
            database.from(userData).selectDistinct(userData.onlineName).where { userData.onlineName eq "Kelair445" }
        println(query.sql)
        query.forEach {
            println(it[userData.onlineName])
        }
    }

    fun close() {
        dataSource.close()
    }
}