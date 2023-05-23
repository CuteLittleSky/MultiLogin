plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    compileOnly(project(":logger"))

    testImplementation(project(":logger"))
}