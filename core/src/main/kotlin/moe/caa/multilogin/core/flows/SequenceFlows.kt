package moe.caa.multilogin.core.flows


/**
 * 代表顺序流
 */
class SequenceFlows<CONTEXT>(
    private val steps: List<BaseFlows<CONTEXT>>
) : BaseFlows<CONTEXT>() {
    override fun run(context: CONTEXT): Signal {
        for (step in steps) {
            val signal = step.run(context)
            // PASS， 继续执行
            if (signal === Signal.PASSED) continue
            // 中断
            if (signal === Signal.TERMINATED) return Signal.TERMINATED
        }
        return Signal.PASSED
    }
}