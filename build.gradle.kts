plugins {
    kotlin("jvm") version "2.0.20"
    id("com.gradleup.shadow") version "8.3.0"
}

group = "cc.mewcraft.orientation"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.mewcraft.cc/private") {
        credentials {
            username = project.providers.gradleProperty("nyaadanbouUsername").getOrElse("")
            password = project.providers.gradleProperty("nyaadanbouPassword").getOrElse("")
        }
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    compileOnly("cc.mewcraft:playtime-paper:1.0-SNAPSHOT")

    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC.2")

    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.19.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.19.0")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}
