enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.60.4"
}

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    versionCatalogs {
        val versionCatalogPath = "../../gradle/libs.versions.toml"
        if (file(versionCatalogPath).exists()) {
            create("libs") {
                from(files(versionCatalogPath))
            }
        }
    }
}

rootProject.name = "buildLogic"

include(
    ":plugins"
)
