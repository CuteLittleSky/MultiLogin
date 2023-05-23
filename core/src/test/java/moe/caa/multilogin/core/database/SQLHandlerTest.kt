package moe.caa.multilogin.core.database

import org.junit.Test
import org.ktorm.dsl.forEach
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import org.spongepowered.configurate.yaml.YamlConfigurationLoader

class SQLHandlerTest {

    @Test
    fun initTest() {
        val build = YamlConfigurationLoader.builder().buildAndLoadString(
            """
            backend: "MYSQL"
            tablePrefix: multilogin
            properties: 
              jdbcUrl: "jdbc:mysql://localhost:3306/multilogin?useSSL=false"
              dataSource:
                user: "root"
                password: "111111"
        """.trimIndent()
        )


        val sqlHandler = SQLHandler()
        sqlHandler.init(build)
        sqlHandler.database.from(sqlHandler.userData)
            .select(sqlHandler.userData.onlineUUID).forEach {
                println(it[sqlHandler.userData.onlineUUID])
            }
    }
}