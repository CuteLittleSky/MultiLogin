package moe.caa.multilogin.core.flows

import moe.caa.multilogin.core.main.MultiCore
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean


class EntrustFlows<CONTEXT : Any>(
    private val steps: List<BaseFlows<CONTEXT>>
) : BaseFlows<CONTEXT>() {
    override fun run(context: CONTEXT): Signal {
        // 存放成功的标志信号
        val passed = AtomicBoolean(false)
        // 信号
        val latch = CountDownLatch(1)
        // 存放当前有多少工序加工
        val currentTasks: MutableList<BaseFlows<CONTEXT>> = Collections.synchronizedList(ArrayList())
        // 避免阻死
        var flag = false
        for (step in steps) {
            flag = true;
            currentTasks.add(step)
            MultiCore.executorService.execute {
                try {
                    val signal = step.run(context)
                    // 这个工序能完成这项任务，释放信号
                    if (signal === Signal.PASSED) {
                        passed.set(true)
                        latch.countDown()
                    }
                } finally {
                    currentTasks.remove(step)
                    // 没人能完成这个工序，释放信号
                    if (currentTasks.isEmpty()) latch.countDown()
                }
            }
        }

        if (flag) latch.await()
        return if (passed.get()) Signal.PASSED else Signal.TERMINATED
    }
}