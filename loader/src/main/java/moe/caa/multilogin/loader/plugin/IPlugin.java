package moe.caa.multilogin.loader.plugin;

import moe.caa.multilogin.loader.main.PluginLoader;

import java.io.File;

/**
 * 公共插件接口
 */
public interface IPlugin {
    /**
     * 获得数据文件存放路径
     *
     * @return 数据文件路径
     */
    File getDataFolder();

    /**
     * 获得配置存放路径
     *
     * @return 配置存放路径
     */
    File getConfigFolder();

    /**
     * 获得依赖文件夹
     *
     * @return 依赖文件夹
     */
    File getLibrariesFolder();

    /**
     * 获得临时目录文件夹
     *
     * @return 临时目录文件夹
     */
    File getTempFolder();

    /**
     * 获得插件类加载工具
     *
     * @return 插件类加载工具
     */
    PluginLoader getPluginLoader();
}
