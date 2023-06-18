package moe.caa.multilogin.loader.injector;

import moe.caa.multilogin.loader.core.IMultiCore;

/**
 * 子模块注入接口
 */
public interface IInjector {

    /**
     * 开始注入
     */
    void inject(IMultiCore api) throws Throwable;
}