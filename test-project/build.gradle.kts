import uk.gov.pipelines.config.ApkConfig

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("uk.gov.pipelines.android-lib-config") apply false
}

buildscript {
    // The Github repository name
    val projectKey: String by rootProject.extra("mobile-android-pipelines")

    // The Maven group ID
    val projectId: String by rootProject.extra("uk.gov.pipelines")

    // Relative directory of the buildLogic included build
    val buildLogicDir: String by rootProject.extra("../buildLogic")
}

val apkConfig by rootProject.extra(
    object: ApkConfig {
        override val applicationId: String = "uk.gov.pipelines.testproject"
        override val debugVersion: String = "DEBUG_VERSION"
        override val sdkVersions = object: ApkConfig.SdkVersions {
            override val minimum = 29
            override val target = 33
            override val compile = 34
        }
    }
)
