import moe.caa.multilogin.gradle.librarycollector.cloud
import moe.caa.multilogin.gradle.librarycollector.netty

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(project(":multilogin-api"))
    compileOnly(project(":multilogin-loader"))
    compileOnly(project(":multilogin-core"))
    compileOnly(project(":multilogin-velocity"))

    compileOnly(fileTree(File("libraries")))
    compileOnly(cloud("velocity"))
    compileOnly(netty("all"))
}

tasks.shadowJar {
    archiveFileName = "MultiLogin-Velocity-Core"

    configurations = listOf()
}