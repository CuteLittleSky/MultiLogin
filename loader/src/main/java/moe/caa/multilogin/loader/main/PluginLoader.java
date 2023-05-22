package moe.caa.multilogin.loader.main;

import moe.caa.multilogin.loader.exception.InitialFailedException;
import moe.caa.multilogin.loader.library.Library;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PluginLoader {
    public static final Map<Library, String> libraryDigestMap;

    static {
        try (InputStream digestsStream = PluginLoader.class.getClassLoader().getResourceAsStream(".digests");
             InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(digestsStream));
             LineNumberReader lnr = new LineNumberReader(isr)){
            Map<Library, String> tMap = new HashMap<>();
            lnr.lines().map(s -> s.split("="))
                    .forEach(ss -> tMap.put(Library.of(ss[0], ":"), ss[1]));
            libraryDigestMap = Collections.unmodifiableMap(tMap);
        } catch (IOException e){
            throw new InitialFailedException("inner", e);
        }
    }
}
