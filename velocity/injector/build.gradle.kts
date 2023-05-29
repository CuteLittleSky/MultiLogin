plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    compileOnly(project(":core"))
    compileOnly(project(":logger"))
    compileOnly(project(":loader"))

    compileOnly("com.velocitypowered:velocity-proxy:3.2.0")
}

repositories{
    maven("https://nexus.ksnb.fun/repository/multilogin/")
}