package moe.caa.multilogin.core.flows

/**
 * 表示一个加工流
 */
enum class Signal {

    /**
     * 通过
     */
    PASSED,

    /**
     * 异常终止
     */
    TERMINATED;
}