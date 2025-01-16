import uk.gov.pipelines.config.AndroidSdkVersions
import uk.gov.pipelines.emulator.EmulatorConfig
import uk.gov.pipelines.emulator.SystemImageSource

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("uk.gov.pipelines.android-root-config")
    id("uk.gov.pipelines.android-lib-config") apply false
}

buildscript {
    val githubRepositoryName: String by rootProject.extra("mobile-android-pipelines")

    val mavenGroupId: String by rootProject.extra("uk.gov.pipelines")

    // Relative directory of the included build logic build
    val buildLogicDir: String by rootProject.extra("../buildLogic")

    val sonarProperties: Map<String, String> by rootProject.extra(
        mapOf(
            "sonar.projectKey" to "mobile-android-pipelines-test-project",
            "sonar.projectName" to "mobile-android-pipelines-test-project",
        )
    )

    // https://issuetracker.google.com/issues/380600747
    dependencies {
        classpath("org.bouncycastle:bcutil-jdk18on:1.79")
    }
}

val androidSdkVersions by rootProject.extra(
    AndroidSdkVersions(
        minSdk = 29,
        targetSdk = 34,
        compileSdk = 35,
    )
)

val emulatorConfig by rootProject.extra(
    EmulatorConfig(
        systemImageSources = setOf(SystemImageSource.AOSP_ATD),
        androidApiLevels = setOf(
            // Cannot use Android Test Device (ATD) below API 30
            30,
            // The emulator would not start in the Github Actions runner when using API 34
            33,
        ),
        deviceFilters = setOf("Pixel XL"),
    )
)
