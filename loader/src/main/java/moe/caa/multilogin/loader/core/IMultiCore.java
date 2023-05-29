package moe.caa.multilogin.loader.core;

/**
 * 表示 MultiCore
 */
public interface IMultiCore {
    /**
     * 初始化
     */
    void init() throws Exception;

    /**
     * 关闭
     */
    void close() throws Exception;
}
