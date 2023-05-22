package moe.caa.multilogin.loader.library;

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
        return String.format("%s/%s/%s/%s-%s.jar", group.replace(".", "/"), name, version, name, version);
    }
}
