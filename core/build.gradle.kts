plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    compileOnly(project(":logger"))
    compileOnly(project(":loader"))
    testImplementation(project(":logger"))
}