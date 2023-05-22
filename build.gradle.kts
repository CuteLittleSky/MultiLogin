import java.nio.file.Files
import java.security.MessageDigest

plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    id("com.github.johnrengelman.shadow") version "8.1.0" apply false
}

dependencies {
    compileOnly("com.zaxxer", "HikariCP",  "4.0.3")
    compileOnly("com.google.code.gson", "gson", "2.8.8")
    compileOnly("org.spongepowered", "configurate-yaml", "4.1.2")
    compileOnly("org.spongepowered", "configurate-core", "4.1.2")
    compileOnly("org.yaml", "snakeyaml", "1.33")
    compileOnly("com.squareup.okhttp3", "okhttp", "4.10.0")
    compileOnly("com.mojang", "brigadier", "1.0.18")
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(11)
    }
}
