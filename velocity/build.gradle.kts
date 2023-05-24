plugins {
    id("java")
    id ("net.nemerosa.versioning")
}

dependencies {
    implementation(project(":loader"))
    implementation(project(":logger"))

    compileOnly(annotationProcessor("com.velocitypowered","velocity-api","3.1.0"))
}

tasks {
    jar{
        dependsOn(":core:jar")
        from(project(":core").tasks.getByName("jar").outputs.files.singleFile)
        from(zipTree(project(":loader").tasks.getByName("jar").outputs.files.singleFile))
        from(zipTree(project(":logger").tasks.getByName("jar").outputs.files.singleFile))

        archiveFileName.set("MultiLogin-Velocity-v${versioning.info.build}.jar")
        destinationDirectory.set(File(rootDir, "jar"))
    }
}