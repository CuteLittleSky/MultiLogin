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
        dependsOn(":core:jar", ":velocity:injector:jar")

        from(zipTree(project(":loader").tasks.getByName("jar").outputs.files.singleFile))
        from(zipTree(project(":logger").tasks.getByName("jar").outputs.files.singleFile))
        from(project(":core").tasks.getByName("jar").outputs.files.singleFile) {
            rename { "MultiLogin-Core" }
        }
        from(project(":velocity:injector").tasks.getByName("jar").outputs.files.singleFile) {
            rename { "MultiLogin-Velocity-Injector" }
        }

        archiveFileName.set("MultiLogin-Velocity-v${versioning.info.build}.jar")
        destinationDirectory.set(File(rootDir, "jar"))
    }
}