package moe.caa.multilogin.core.ohc

import moe.caa.multilogin.logger.LoggerProvider
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Http 日志打印拦截器
 */
class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        LoggerProvider.getLogger().debug(java.lang.String.format("--> %s %s", request.method, request.url))

        val requestBody: RequestBody? = request.body
        if (requestBody != null) {
            val bf = okio.Buffer()
            requestBody.writeTo(bf)
            val size: Long = bf.size
            if (size > 0) LoggerProvider.getLogger().debug(String.format("--> (%d bytes)", size))
        }

        val startNs = System.nanoTime()
        val response: Response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            LoggerProvider.getLogger().debug("<-- HTTP FAILED", e)
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        LoggerProvider.getLogger().debug(String.format("<-- %s %s (%dms)", response.code, response.request.url, tookMs))
        val body = response.body
        if (body != null) {
            val source = body.source()
            source.request(Long.MAX_VALUE)
            val buffer: okio.Buffer = source.buffer
            val size: Long = buffer.size
            if (size > 0) LoggerProvider.getLogger().debug(String.format("<-- (%d bytes)", size))
        }

        return response
    }
}