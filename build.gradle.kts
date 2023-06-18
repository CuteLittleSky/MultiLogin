import groovy.json.JsonOutput

version = "0.7.0-ALPHA.1"

plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
    id("net.nemerosa.versioning") version "3.0.0" apply false
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
    maven("https://repo1.maven.org/maven2")
    maven("https://libraries.minecraft.net")
}



allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        // sql
        testImplementation(compileOnly("com.zaxxer", "HikariCP", "5.0.1"))
        testImplementation(compileOnly("org.jetbrains.exposed", "exposed-core", "0.41.1"))
        testImplementation(compileOnly("org.jetbrains.exposed", "exposed-jdbc", "0.41.1"))
        testImplementation(compileOnly("mysql", "mysql-connector-java", "8.0.33"))

        // config
        testImplementation(compileOnly("org.spongepowered", "configurate-yaml", "4.1.2"))
        testImplementation(compileOnly("org.spongepowered", "configurate-core", "4.1.2"))
        testImplementation(compileOnly("org.yaml", "snakeyaml", "2.0"))

        testImplementation(compileOnly("io.leangen.geantyref", "geantyref", "1.3.14"))
        testImplementation(compileOnly("com.google.code.gson", "gson", "2.8.8"))
        testImplementation(compileOnly("com.squareup.okhttp3", "okhttp", "4.10.0"))
        testImplementation(compileOnly("com.mojang", "brigadier", "1.0.18"))

        // kotlin runtime
        testImplementation(compileOnly("org.jetbrains.kotlin", "kotlin-stdlib", "1.8.21"))
        testImplementation(compileOnly("org.jetbrains.kotlin", "kotlin-stdlib-common", "1.8.21"))


        testImplementation("junit:junit:4.13.1")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
    }

    tasks.withType<Jar> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    tasks.processResources{
        val contributors = HashSet<String>()

        File(rootDir, "contributors").readLines().forEach {
            if (it.trim().isNotEmpty() && (it[0] != '#')) contributors.add(it)
        }


        filter { it
            .replace("@contributors_json@", JsonOutput.toJson(contributors))
            .replace("@contributors@", contributors.joinToString { ", " })
            .replace("@build_timestamp@", System.currentTimeMillis().toString())
            .replace("@version@", rootProject.version.toString())
        }
    }
}