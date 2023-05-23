package moe.caa.multilogin.core.flows

import moe.caa.multilogin.core.main.MultiCore
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean


class ParallelFlows<CONTEXT : Any>(
    private val steps: List<BaseFlows<CONTEXT>>
) : BaseFlows<CONTEXT>() {
    override fun run(context: CONTEXT): Signal {
        // 存放终止信号
        val terminate = AtomicBoolean(false)
        // 信号
        val latch = CountDownLatch(1)
        // 存放当前有多少工序加工
        val currentTasks: MutableList<BaseFlows<CONTEXT>> = Collections.synchronizedList(ArrayList())
        // 避免阻死
        var flag = false
        for (step in steps) {
            flag = true
            currentTasks.add(step)
            MultiCore.executorService.execute {
                try {
                    val signal = step.run(context)
                    if (signal !== Signal.TERMINATED) return@execute
                    // 这个工序不能完成当前任务，释放信号
                    terminate.set(true)
                    latch.countDown()
                } finally {
                    currentTasks.remove(step)
                    // 全部完成这个工序，释放信号
                    if (currentTasks.isEmpty()) latch.countDown()
                }
            }
        }

        if (flag) latch.await()
        return if (terminate.get()) Signal.TERMINATED else Signal.PASSED
    }
}