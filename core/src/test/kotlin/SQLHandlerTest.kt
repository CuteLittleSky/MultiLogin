import moe.caa.multilogin.core.database.SQLHandler
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
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

        transaction {
            sqlHandler.inGameProfile.selectAll().forEach {
                println(it[sqlHandler.inGameProfile.inGameUUID])
            }
        }
    }
}