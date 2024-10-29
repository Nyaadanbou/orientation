import net.minecrell.pluginyml.paper.PaperPluginDescription.RelativeLoadOrder

plugins {
    id("orientation-conventions")
    id("nyaadanbou-conventions.copy-jar")
    alias(local.plugins.pluginyml.paper)
}

version = "1.0.0-SNAPSHOT"

dependencies {
    compileOnly(local.paper)
    compileOnly(local.playtime.paper)

    implementation(local.mccoroutine.bukkit.api) {
        exclude("org.jetbrains.kotlin")
    }
    implementation(local.mccoroutine.bukkit.core) {
        exclude("org.jetbrains.kotlin")
    }

    implementation(platform(libs.bom.cloud.paper))
    implementation(platform(libs.bom.cloud.kotlin))
}

tasks {
    copyJar {
        environment = "paper"
        jarFileName = "orientation-${project.version}.jar"
    }
}

paper {
    main = "cc.mewcraft.orientation.OrientationPlugin"
    name = "Orientation"
    version = "${project.version}"
    apiVersion = "1.21"
    author = "g2213swo"
    serverDependencies {
        register("Playtime") {
            required = true
            load = RelativeLoadOrder.OMIT
        }
    }
}