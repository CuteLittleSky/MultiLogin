package moe.caa.multilogin.core.flows

abstract class BaseFlows<CONTEXT> {
    /**
     * 加工
     */
    abstract fun run(context: CONTEXT): Signal
}