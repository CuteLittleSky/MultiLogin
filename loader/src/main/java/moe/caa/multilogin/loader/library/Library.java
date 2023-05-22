package moe.caa.multilogin.loader.library;

import java.io.File;
import java.util.Objects;

public class Library {
    public final String group;
    public final String name;
    public final String version;

    public Library(String group, String name, String version) {
        this.group = group;
        this.name = name;
        this.version = version;
    }

    public static Library of(String value, String split) {
        final String[] args = value.split(split);
        return new Library(args[0], args[1], args[2]);
    }

    public String getDownloadUrl() {
        // group/name/version/name-version.jar
        return String.format("%s/%s/%s/%s", group.replace(".", "/"), name, version, getFileName());
    }

    public String getFileName() {
        return String.format("%s-%s.jar", name, version);
    }

    public File getFile(File folder) {
        return new File(folder, String.format("%s/%s/%s/%s", group.replace(".", "/"), name, version, getFileName()));
    }

    @Override
    public String toString() {
        return "Library{" + "group='" + group + ", name='" + name + ", version='" + version + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Library library = (Library) o;
        return Objects.equals(group, library.group) && Objects.equals(name, library.name) && Objects.equals(version, library.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, name, version);
    }
}
