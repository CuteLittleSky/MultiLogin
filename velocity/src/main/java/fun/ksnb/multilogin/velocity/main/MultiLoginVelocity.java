package fun.ksnb.multilogin.velocity.main;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ksnb.multilogin.velocity.logger.Slf4jLoggerBridge;
import moe.caa.multilogin.loader.main.PluginLoader;
import moe.caa.multilogin.loader.plugin.IPlugin;
import moe.caa.multilogin.logger.LoggerProvider;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

public class MultiLoginVelocity implements IPlugin {
    private final ProxyServer proxyServer;
    private final PluginLoader pluginLoader;
    private final File dataFolder;
    private final File configFolder;
    private final File librariesFolder;
    private final File tempFolder;
    private boolean initiated;

    @Inject
    public MultiLoginVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) throws Exception {
        this.proxyServer = server;
        this.dataFolder = dataDirectory.toFile();
        this.configFolder = dataDirectory.toFile();
        this.librariesFolder = new File(dataFolder, "libraries");
        this.tempFolder = new File(dataFolder, "tmp");

        LoggerProvider.setLogger(new Slf4jLoggerBridge(logger));

        this.pluginLoader = new PluginLoader(this);
        try {
            this.pluginLoader.loadCoreLibraries();
            this.pluginLoader.loadNestCoreJar();
            this.pluginLoader.loadNestJar("MultiLogin-Velocity-Injector");
            pluginLoader.getInjector("moe.caa.multilogin.velocity.injector.VelocityInjector").inject(pluginLoader.getMultiCore());
            initiated = true;
        } catch (Throwable throwable) {
            LoggerProvider.getLogger().fatal("An exception was encountered while initializing the plugin.", throwable);
            proxyServer.shutdown();
            initiated = false;
        }
    }

    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        if (!initiated) return;
        try {
            pluginLoader.getMultiCore().init();
        } catch (Throwable throwable) {
            LoggerProvider.getLogger().fatal("An exception was encountered while loading the plugin.", throwable);
            proxyServer.shutdown();
            return;
        }
    }

    @Subscribe
    public void onDisable(ProxyShutdownEvent event) {
        if (!initiated) return;
        try {
            pluginLoader.getMultiCore().close();
            pluginLoader.close();
        } catch (Throwable e) {
            LoggerProvider.getLogger().error("An exception was encountered while close the plugin", e);
        } finally {
            proxyServer.shutdown();
        }
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public File getConfigFolder() {
        return configFolder;
    }

    @Override
    public File getLibrariesFolder() {
        return librariesFolder;
    }

    @Override
    public File getTempFolder() {
        return tempFolder;
    }

    @Override
    public PluginLoader getPluginLoader() {
        return pluginLoader;
    }
}
