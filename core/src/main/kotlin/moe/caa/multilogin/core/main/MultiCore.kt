package moe.caa.multilogin.core.main

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger


class MultiCore {


    companion object {
        private val asyncThreadId = AtomicInteger(0)

        val executorService: ExecutorService = Executors.newCachedThreadPool {
            val thread = Thread(it, "MultiLogin Async #" + asyncThreadId.incrementAndGet())
            thread.isDaemon = true
            return@newCachedThreadPool thread
        }
    }
}