package moe.caa.multilogin.core.config

import okhttp3.Authenticator
import okhttp3.Credentials
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * 代理配置
 */
class ProxyConfig(
    val type: Proxy.Type,
    val hostname: String,
    val port: Int,
    val username: String,
    val password: String
) {

    fun getProxy(): Proxy {
        if (type == Proxy.Type.DIRECT) {
            return Proxy.NO_PROXY
        }
        return Proxy(type, InetSocketAddress(hostname, port))
    }

    fun getProxyAuthenticator(): Authenticator {
        return Authenticator { _, response ->
            if (username.isEmpty()) return@Authenticator null
            val credential: String = Credentials.basic(username, password)
            response.request.newBuilder()
                .header("Proxy-Authorization", credential)
                .build()
        }
    }
}