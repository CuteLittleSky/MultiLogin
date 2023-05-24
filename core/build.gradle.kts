plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    compileOnly(project(":logger"))
    testImplementation(project(":logger"))
}

tasks {
    jar {
        archiveFileName.set("MultiLogin-Core.JarFile")
    }
}