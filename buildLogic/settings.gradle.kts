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
