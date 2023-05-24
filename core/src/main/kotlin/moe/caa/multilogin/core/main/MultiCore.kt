package moe.caa.multilogin.core.main

import moe.caa.multilogin.logger.LoggerProvider
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger


class MultiCore {

    private fun showBanner() {
        //show banner
        LoggerProvider.getLogger().info("\u001b[40;31m __  __       _ _   _ _                _       \u001b[0m")
        LoggerProvider.getLogger().info("\u001b[40;33m|  \\/  |_   _| | |_(_) |    ___   __ _(_)_ __  \u001b[0m")
        LoggerProvider.getLogger().info("\u001b[40;32m| |\\/| | | | | | __| | |   / _ \\ / _` | | '_ \\ \u001b[0m")
        LoggerProvider.getLogger().info("\u001b[40;36m| |  | | |_| | | |_| | |__| (_) | (_| | | | | |\u001b[0m")
        LoggerProvider.getLogger().info("\u001b[40;34m|_|  |_|\\__,_|_|\\__|_|_____\\___/ \\__, |_|_| |_|\u001b[0m")
        LoggerProvider.getLogger().info("\u001b[40;35m                                 |___/         \u001b[0m")
    }

    fun init() {
        showBanner()
    }

    companion object {
        private val asyncThreadId = AtomicInteger(0)

        val executorService: ExecutorService = Executors.newCachedThreadPool {
            val thread = Thread(it, "MultiLogin Async #" + asyncThreadId.incrementAndGet())
            thread.isDaemon = true
            return@newCachedThreadPool thread
        }
    }
}