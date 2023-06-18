package moe.caa.multilogin.core.config.service

import moe.caa.multilogin.core.config.ConfException
import java.util.*
import java.util.function.BiFunction


/**
 * 表示一个基础的 Service 配置
 */
abstract class BaseServiceConfig(
    val id: Int,
    val name: String,
    val initUUID: InitUUID,
    val whitelist: Boolean
) {
    init {
        if (this.id > 127 || this.id < 0)
            throw ConfException(
                "Service id $id is out of bounds, The value can only be between 0 and 127."
            )
    }

    /**
     * 初始化的UUID生成器
     */
    enum class InitUUID(private val biFunction: BiFunction<UUID, String, UUID>) {
        DEFAULT(BiFunction<UUID, String, UUID> { u: UUID, _ -> u }),
        OFFLINE(BiFunction<UUID, String, UUID> { _, n: String -> UUID.nameUUIDFromBytes(
            "OfflinePlayer:$n".toByteArray(java.nio.charset.StandardCharsets.UTF_8)
        )}),
        RANDOM(BiFunction<UUID, String, UUID> { _, _ -> UUID.randomUUID() });

        fun generateUUID(onlineUUID: UUID, currentUsername: String): UUID {
            return biFunction.apply(onlineUUID, currentUsername)
        }
    }




}