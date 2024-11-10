plugins {
    id("orientation-conventions")
    `maven-publish`
}

group = "cc.mewcraft.orientation"
version = "1.0.0-SNAPSHOT"

dependencies {
    compileOnly(local.paper)
}

publishing {
    repositories {
        maven("https://repo.mewcraft.cc/private") {
            credentials {
                username = providers.gradleProperty("nyaadanbou.mavenUsername").orNull
                password = providers.gradleProperty("nyaadanbou.mavenPassword").orNull
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            artifactId = "orientation-api"
            from(components["java"])
        }
    }
}