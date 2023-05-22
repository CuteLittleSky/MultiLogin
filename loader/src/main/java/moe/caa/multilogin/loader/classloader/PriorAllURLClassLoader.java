package moe.caa.multilogin.loader.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有优先类加载器
 */
public class PriorAllURLClassLoader extends URLClassLoader {
    static {
        registerAsParallelCapable();
    }

    private final List<String> ignored;

    public PriorAllURLClassLoader(ClassLoader parent, List<String> ignored) {
        super(new URL[0], parent);
        this.ignored = new ArrayList<>(ignored);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                if (containPrior(name)) {
                    try {
                        c = findClass(name);
                        if (resolve) resolveClass(c);
                        return c;
                    } catch (ClassNotFoundException ignored) {
                    }
                }
            }
        }
        return super.loadClass(name, resolve);
    }

    public boolean containPrior(String name) {
        return !containIgnore(name);
    }

    private boolean containIgnore(String name) {
        for (String s : ignored) {
            if (name.startsWith(s)) return true;
        }
        return false;
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}