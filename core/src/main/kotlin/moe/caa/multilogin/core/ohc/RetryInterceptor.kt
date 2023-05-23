package moe.caa.multilogin.core.ohc

import moe.caa.multilogin.logger.LoggerProvider
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 延时重试拦截器
 */
class RetryInterceptor(
    private val retry: Int,
    private val delay: Long
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var tc = 0
        while (true) {
            try {
                return chain.proceed(request)
            } catch (e: IOException) {
                LoggerProvider.getLogger().debug("$tc retry failed.", e)
                if (tc >= retry) throw e
            }
            TimeUnit.MILLISECONDS.sleep(delay)
            tc++
            LoggerProvider.getLogger().debug("--> $tc retry.")
        }
    }
}