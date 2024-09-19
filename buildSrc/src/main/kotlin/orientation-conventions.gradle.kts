plugins {
    kotlin("jvm")
    id("com.gradleup.shadow")
}

group = "cc.mewcraft.orientation"

val local = the<org.gradle.accessors.dm.LibrariesForLocal>()

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://repo.mewcraft.cc/private") {
        credentials {
            username = project.providers.gradleProperty("nyaadanbouUsername").getOrElse("")
            password = project.providers.gradleProperty("nyaadanbouPassword").getOrElse("")
        }
    }
}

kotlin {
    jvmToolchain(21)

    sourceSets {
        val main by getting {
            dependencies {
                compileOnly(kotlin("stdlib"))
                compileOnly(kotlin("reflect"))
                compileOnly(local.kotlinx.coroutines.core)
            }
        }

        val test by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(local.kotlinx.coroutines.core)
            }
        }
    }
}
