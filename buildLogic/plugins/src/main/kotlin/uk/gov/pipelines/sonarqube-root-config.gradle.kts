package uk.gov.pipelines

import org.sonarqube.gradle.SonarExtension
import uk.gov.pipelines.extensions.ProjectExtensions.versionCode
import uk.gov.pipelines.extensions.ProjectExtensions.versionName

plugins {
    id("org.sonarqube")
}

configure<SonarExtension> {
    this.setAndroidVariant("debug")

    val projectKey: String by project.rootProject.extra
    val rootSonarProperties by rootProject.extra(
        mapOf(
            "sonar.projectKey" to projectKey,
            "sonar.projectName" to projectKey,
            "sonar.host.url" to "https://sonarcloud.io",
            "sonar.token" to System.getProperty("SONAR_TOKEN"),
            "sonar.projectVersion" to "${project.versionName}-${project.versionCode}",
            "sonar.organization" to "govuk-one-login",
            "sonar.sourceEncoding" to "UTF-8",
            "sonar.sources" to ""
        )
    )

    properties {
        rootSonarProperties.forEach { (key, value) ->
            property(key, value)
        }
    }
}
