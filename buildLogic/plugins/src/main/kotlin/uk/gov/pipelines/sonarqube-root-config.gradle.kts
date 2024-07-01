package uk.gov.pipelines

import org.gradle.kotlin.dsl.extra
import org.sonarqube.gradle.SonarExtension
import uk.gov.pipelines.extensions.ProjectExtensions.versionCode
import uk.gov.pipelines.extensions.ProjectExtensions.versionName

plugins {
    id("org.sonarqube")
}

/**
 * Defined within the git repository's `build.gradle.kts` file
 */
val rootSonarProperties by rootProject.extra(
    mapOf(
        "sonar.host.url" to "https://sonarcloud.io",
        "sonar.token" to System.getProperty("SONAR_TOKEN"),
        "sonar.projectKey" to "mobile-android-logging",
        "sonar.projectName" to "mobile-android-logging",
        "sonar.projectVersion" to "${project.versionName}-${project.versionCode}",
        "sonar.organization" to "govuk-one-login",
        "sonar.sourceEncoding" to "UTF-8",
        "sonar.sources" to ""
    )
)

configure<SonarExtension> {
    this.setAndroidVariant("debug")

    properties {
        rootSonarProperties.forEach { (key, value) ->
            property(key, value)
        }
    }
}
