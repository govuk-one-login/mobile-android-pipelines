import uk.gov.pipelines.config.ApkConfig
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
}

val apkConfig by rootProject.extra(
    object: ApkConfig {
        override val applicationId: String = "uk.gov.pipelines.testproject"
        override val debugVersion: String = "DEBUG_VERSION"
        override val sdkVersions = object: ApkConfig.SdkVersions {
            override val minimum = 29
            override val target = 36
            override val compile = 36
        }
    }
)

val emulatorConfig by rootProject.extra(
    EmulatorConfig(
        systemImageSources = setOf(SystemImageSource.AOSP_ATD),
        androidApiLevels = setOf(
            34,
            37,
        ),
        deviceFilters = setOf("Pixel XL"),
    )
)
