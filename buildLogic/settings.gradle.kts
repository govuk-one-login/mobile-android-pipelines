enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    versionCatalogs {
        val versionCatalogPath = "../../gradle/libs.versions.toml"
        create("libs") {
            if (file(versionCatalogPath).exists()) {
                from(files(versionCatalogPath))
            } else {
                from(files("libs.versions.toml"))
            }
        }
    }
}

rootProject.name = "buildLogic"

include(":plugins")
