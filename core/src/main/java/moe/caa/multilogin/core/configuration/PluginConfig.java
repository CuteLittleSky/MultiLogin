package moe.caa.multilogin.core.configuration;

import lombok.Getter;
import moe.caa.multilogin.api.logger.LoggerProvider;
import moe.caa.multilogin.api.util.IOUtil;
import moe.caa.multilogin.core.configuration.yggdrasil.YggdrasilServiceConfig;
import moe.caa.multilogin.core.configuration.yggdrasil.hasjoined.HasJoinedConfig;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PluginConfig {
    private final File dataFolder;

    @Getter
    private SqlConfig sqlConfig;
    @Getter
    private Map<Integer, YggdrasilServiceConfig> idMap;

    public PluginConfig(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public void reload() throws IOException, URISyntaxException {
        File servicesFolder = new File(dataFolder, "services");
        if (!dataFolder.exists()) {
            Files.createDirectory(dataFolder.toPath());
        }
        if (!servicesFolder.exists()) {
            Files.createDirectory(servicesFolder.toPath());
        }

        saveResource("config.yml", false);
        saveResourceDir("examples", true);

        CommentedConfigurationNode configConfigurationNode =
                YamlConfigurationLoader.builder().file(new File(dataFolder, "config.yml")).build().load();

        sqlConfig = SqlConfig.read(configConfigurationNode.node("sql"));

        Map<Integer, YggdrasilServiceConfig> idMap = new HashMap<>();
        try (Stream<Path> list = Files.list(servicesFolder.toPath())) {
            List<YggdrasilServiceConfig> tmp = new ArrayList<>();
            list.forEach(path -> {
                try {
                    tmp.add(YggdrasilServiceConfig.read(YamlConfigurationLoader.builder().path(path).build().load()));
                } catch (Exception e) {
                    LoggerProvider.getLogger().error(new ConfException("Unable to read Yggdrasil config under file " + path, e));
                }
            });

            for (YggdrasilServiceConfig config : tmp) {
                if (idMap.containsKey(config.getId())) {
                    throw new ConfException(String.format("The same yggdrasil id value %d exists.", config.getId()));
                }
                idMap.put(config.getId(), config);
            }
        }

        List<HasJoinedConfig> collect = idMap.values().stream().map(YggdrasilServiceConfig::getHasJoined)
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                .entrySet().stream().filter(e -> e.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
        for (HasJoinedConfig ignored : collect) {
            throw new ConfException("There are duplicate configurations of hasJoined.");
        }

        idMap.forEach((i, y) -> {
            LoggerProvider.getLogger().info(String.format(
                    "Add a yggdrasil service with id %d and name %s.", i, y.getName()
            ));
        });

        if (idMap.size() == 0) LoggerProvider.getLogger().warn(
                "The server has not added any yggdrasil service, which will prevent all players from logging in."
        );
        else LoggerProvider.getLogger().info(String.format(
                "Added %d Yggdrasil services.", idMap.size()
        ));
        this.idMap = Collections.unmodifiableMap(idMap);
    }

    public void saveResource(String path, boolean cover) throws IOException {
        File file = new File(dataFolder, path);
        boolean exists = file.exists();
        if (exists && !cover) {
            return;
        } else {
            if (!exists) Files.createFile(file.toPath());
        }
        try (InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/" + path));
             FileOutputStream fs = new FileOutputStream(file)) {
            IOUtil.copy(is, fs);
        }
        if (!exists) {
            LoggerProvider.getLogger().info("Extract: " + path);
        } else {
            LoggerProvider.getLogger().info("Cover: " + path);
        }
    }

    public void saveResourceDir(String path, boolean cover) throws IOException, URISyntaxException {
        File file = new File(dataFolder, path);
        if (!file.exists()) Files.createDirectory(file.toPath());
        try (JarFile jarFile = new JarFile(new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()))) {
            List<JarEntry> jarFiles = jarFile.stream().filter(jarEntry -> jarEntry.getRealName().startsWith(path)).filter(jarEntry -> !jarEntry.getRealName().equals(path + "/")).collect(Collectors.toList());
            for (JarEntry je : jarFiles) {
//                if (je.isDirectory()) {
//                暂时不考虑目录下目录情况
//                    预留
//                } else {
//                下属文件
                String realName = je.getRealName();
                String fileName = realName.substring(path.length());
                File subFile = new File(file, fileName);
                boolean exists = subFile.exists();
                if (exists && !cover) {
                    return;
                } else {
                    if (!exists) Files.createFile(subFile.toPath());
                }
                try (InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/" + realName));
                     FileOutputStream fs = new FileOutputStream(subFile)) {
                    IOUtil.copy(is, fs);
                }
                if (!exists) {
                    LoggerProvider.getLogger().info("Extract: " + realName);
                } else {
                    LoggerProvider.getLogger().info("Cover: " + realName);
                }
//                }
            }
        }
    }
}
