import java.util.Properties

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

// This project is expected to be included in a parent build
val parentProjectDir = "../.."

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    versionCatalogs {
        val overrideLibsVersions = parentProjectProperties()
            .getProperty("uk.gov.pipelines.overrideLibsVersions")
            .toBoolean()
        val versionCatalogPath = "${parentProjectDir}/gradle/libs.versions.toml"
        create("libs") {
            if (overrideLibsVersions && file(versionCatalogPath).exists()) {
                from(files(versionCatalogPath))
            } else {
                from(files("libs.versions.toml"))
            }
        }
    }
}

rootProject.name = "buildLogic"

include(":plugins")

private fun parentProjectProperties(): Properties {
    val props = Properties()
    file("${parentProjectDir}/gradle.properties").inputStream().use {
        props.load(it)
    }
    return props
}
