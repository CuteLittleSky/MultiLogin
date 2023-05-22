package moe.caa.multilogin.loader.main;

import moe.caa.multilogin.loader.library.Library;
import org.junit.Test;

import java.io.File;

public class PluginLoaderTest {

    @Test
    public void init() throws Exception {
        PluginLoader test = new PluginLoader(new File(".test"));
        test.loadLibrary(new Library("com.zaxxer", "HikariCP", "4.0.3"));
    }
}
