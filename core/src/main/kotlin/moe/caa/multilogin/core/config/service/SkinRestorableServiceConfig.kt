package moe.caa.multilogin.core.config.service

import moe.caa.multilogin.core.config.SkinRestorerConfig

/**
 * 可修复皮肤的基础 Service 配置
 */
abstract class SkinRestorableServiceConfig(
    id: Int, name: String, initUUID: InitUUID, whitelist: Boolean,
    val skinRestorer: SkinRestorerConfig
) : BaseServiceConfig(
    id, name, initUUID, whitelist
) {
}