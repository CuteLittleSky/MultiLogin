package moe.caa.multilogin.loader.main;

import moe.caa.multilogin.loader.classloader.PriorAllURLClassLoader;
import moe.caa.multilogin.loader.exception.InitialFailedException;
import moe.caa.multilogin.loader.exception.LibraryException;
import moe.caa.multilogin.loader.library.Library;
import moe.caa.multilogin.logger.LoggerProvider;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 表示插件加载器
 */
public class PluginLoader {
    public static final Map<Library, String> libraryDigestMap;
    public static final List<String> repositories;

    static {
        try (InputStream digestsStream = PluginLoader.class.getClassLoader().getResourceAsStream(".digests");
             InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(digestsStream));
             LineNumberReader lnr = new LineNumberReader(isr)) {
            Map<Library, String> tMap = new HashMap<>();
            lnr.lines().map(s -> s.split("="))
                    .forEach(ss -> tMap.put(Library.of(ss[0], ":"), ss[1]));
            libraryDigestMap = Collections.unmodifiableMap(tMap);
        } catch (IOException e) {
            throw new InitialFailedException(".digests", e);
        }

        try (InputStream resourceAsStream = PluginLoader.class.getClassLoader().getResourceAsStream("repositories");
             InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(resourceAsStream));
             LineNumberReader lnr = new LineNumberReader(isr)
        ) {
            LinkedList<String> tList = new LinkedList<>();
            lnr.lines().filter(s -> !s.trim().isEmpty() && s.charAt(0) != '#')
                    .map(s -> s.endsWith("/") ? s : s + '/')
                    .forEach(tList::add);

            repositories = Collections.unmodifiableList(tList);
        } catch (Exception e) {
            throw new InitialFailedException("repositories", e);
        }
    }

    private final File librariesFolder;

    private final PriorAllURLClassLoader classLoader = new PriorAllURLClassLoader(PluginLoader.class.getClassLoader(), List.of(
            "moe.caa.multilogin."
    ));
    private final Set<Library> loaded = new HashSet<>();

    public PluginLoader(File librariesFolder) {
        this.librariesFolder = librariesFolder;
    }

    private static byte[] getBytes(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(false);
        httpURLConnection.setConnectTimeout(10000);
        httpURLConnection.connect();

        if (httpURLConnection.getResponseCode() == 200) {
            try (InputStream input = httpURLConnection.getInputStream()) {
                return input.readAllBytes();
            }
        }
        throw new IOException(String.valueOf(httpURLConnection.getResponseCode()));
    }

    public void loadLibrary(Library library) throws Exception {
        if (loaded.contains(library)) {
            return;
        }
        String digest = libraryDigestMap.get(library);
        if (digest == null) throw new LibraryException(String.format("No digest value found for library %s.", library));
        File libraryFile = library.getFile(librariesFolder);
        if (libraryFile.exists()) {
            if (digest.equals(calcSha256(libraryFile))) {
                classLoader.addURL(libraryFile.toURI().toURL());
                loaded.add(library);
                return;
            }
            LoggerProvider.getLogger().warn(
                    String.format("Failed to validate digest value of file %s, it will be re-downloaded.", libraryFile.getAbsolutePath())
            );
        }

        if (!libraryFile.exists()) {
            if (!libraryFile.getParentFile().exists()) {
                Files.createDirectories(libraryFile.getParentFile().toPath());
            }
        }
        Files.write(libraryFile.toPath(), downloadFromRepositories(library.getDownloadUrl()));

        if (digest.equals(calcSha256(libraryFile))) {
            classLoader.addURL(libraryFile.toURI().toURL());
            loaded.add(library);
        } else {
            throw new LibraryException(String.format("Unable to verify digest value for newly downloaded file %s, giving up.", library));
        }
    }

    public void close() throws IOException {
        classLoader.close();
    }

    private String calcSha256(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        instance.update(Files.readAllBytes(file.toPath()));
        StringBuilder sb = new StringBuilder();
        for (byte b : instance.digest()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private byte[] downloadFromRepositories(String baseUrl) throws Exception {
        Exception exception = null;
        for (String repository : repositories) {
            String spec = repository + baseUrl;
            LoggerProvider.getLogger().debug("Downloading: " + spec);
            try {
                URL url = new URL(spec);
                return getBytes(url);
            } catch (Exception e) {
                Exception libraryException = new LibraryException(spec, e);
                if (exception == null) {
                    exception = libraryException;
                } else {
                    exception.addSuppressed(libraryException);
                }
            }
        }
        throw new LibraryException("Library download failed.", exception);
    }
}
