package uk.gov.pipelines

import org.sonarqube.gradle.SonarExtension
import uk.gov.pipelines.extensions.ProjectExtensions.versionCode
import uk.gov.pipelines.extensions.ProjectExtensions.versionName

plugins {
    id("org.sonarqube")
}

configure<SonarExtension> {
    this.setAndroidVariant("debug")

    val sonarProperties: Map<String, String> by rootProject.extra
    val defaultSonarProperties =
        mapOf(
            "sonar.host.url" to "https://sonarcloud.io",
            "sonar.token" to System.getProperty("SONAR_TOKEN"),
            "sonar.projectVersion" to "${project.versionName}-${project.versionCode}",
            "sonar.organization" to "govuk-one-login",
            "sonar.sourceEncoding" to "UTF-8",
            "sonar.sources" to "",
        )

    val mergedSonarProperties = defaultSonarProperties + sonarProperties

    properties {
        mergedSonarProperties.forEach { (key, value) ->
            property(key, value)
        }
    }
}
