package fun.ksnb.multilogin.velocity.main;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.ksnb.multilogin.velocity.logger.Slf4jLoggerBridge;
import moe.caa.multilogin.loader.library.Library;
import moe.caa.multilogin.loader.main.PluginLoader;
import moe.caa.multilogin.logger.LoggerProvider;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Plugin(id = "multilogin")
public class MultiLoginVelocity {
    private final ProxyServer proxyServer;
    private final Logger logger;
    private final Path dataDirectory;

    private final PluginLoader pluginLoader;

    @Inject
    public MultiLoginVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) throws Exception {
        this.proxyServer = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

        LoggerProvider.setLogger(new Slf4jLoggerBridge(logger));
        this.pluginLoader = new PluginLoader(new File(dataDirectory.toFile(), "libraries"));
        pluginLoader.loadLibrary(new Library("com.zaxxer", "HikariCP", "5.0.1"));
        pluginLoader.addURL(MultiLoginVelocity.class.getResource("MultiLogin-Core.JarFile"));

    }
}
