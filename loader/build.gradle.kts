import java.security.MessageDigest

plugins {
    id("java")
}

val outputSha256 by tasks.registering {
    doLast {
        val dependenciesSha256 =
            rootProject.configurations.compileClasspath.get().resolvedConfiguration.resolvedArtifacts.map { artifact ->
                "${artifact.moduleVersion.id.group}:${artifact.moduleVersion.id.name}:${artifact.moduleVersion.id.version}=" +
                        artifact.file.toPath().toFile().inputStream().use { input ->
                            MessageDigest.getInstance("SHA-256").apply {
                                update(input.readAllBytes())
                            }.digest().joinToString("") {
                                "%02x".format(it)
                            }
                        }
            }

        rootProject.file(".digests").writeText(dependenciesSha256.joinToString("\n"))
    }
}

tasks.processResources {
    dependsOn(outputSha256)
}


tasks.processResources {
    from(rootDir){
        include(".digests")
    }
}