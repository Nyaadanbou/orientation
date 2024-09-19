rootProject.name = "orientation"

dependencyResolutionManagement {
    versionCatalogs {
        create("local") {
            from(files("gradle/local.versions.toml"))
        }
    }
}

include("plugin")