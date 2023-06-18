package moe.caa.multilogin.core.config

/**
 * 表示一个皮肤修复配置
 */
class SkinRestorerConfig(
    val restorer: RestorerType,
    val method: Method,
    val key: String,
    val timeout: Int,
    val retry: Int,
    val retryDelay: Int,
    val proxy: ProxyConfig
){


    /**
     * 在什么时候进行修复
     */
    enum class RestorerType {
        OFF,
        LOGIN,
        ASYNC
    }

    /**
     * 修复方式
     */
    enum class Method {
        URL,
        UPLOAD
    }

}