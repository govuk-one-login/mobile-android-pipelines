package uk.gov.pipelines

import org.sonarqube.gradle.SonarExtension
import java.io.File

plugins {
    id("org.sonarqube")
}

fun generateCommaSeparatedFiles(iterator: Iterable<String>) =
    fileTree(project.projectDir) {
        this.setIncludes(iterator)
    }.files.joinToString(
        separator = ",",
        transform = File::getAbsolutePath,
    )

val androidLintReportFiles =
    generateCommaSeparatedFiles(listOf("**/reports/lint-results-*.xml"))
val detektReportFiles =
    generateCommaSeparatedFiles(
        listOf(
            "**/reports/detekt/*.xml",
        ),
    )
val jacocoXmlReportFiles =
    generateCommaSeparatedFiles(
        listOf(
            // unit test reports, split to stop vale reading line
            "**/reports/jacoco" +
                "/**/*.xml",
            // android instrumentation test reports
            "**/reports/coverage/**/*.xml",
        ),
    )
val junitReportFiles =
    generateCommaSeparatedFiles(
        listOf(
            // instrumentation
            "**/outputs/androidTest-results/managedDevice/*",
            // unit tests
            "**/test-results",
        ),
    )

val ktLintReportFiles =
    generateCommaSeparatedFiles(listOf("**/reports/ktlint/**/*.xml"))
val sonarExclusions =
    listOf(
        Filters.androidInstrumentationTests,
        Filters.sonar,
        Filters.testSourceSets,
        Filters.developer,
        Filters.uiTestWrapper,
    ).flatten().joinToString(separator = ",")

val projectSonarProperties =
    mapOf<String, Any>(
        "sonar.exclusions" to sonarExclusions,
        "sonar.androidLint.reportPaths" to androidLintReportFiles,
        "sonar.coverage.jacoco.xmlReportPaths" to jacocoXmlReportFiles,
        "sonar.kotlin.detekt.reportPaths" to detektReportFiles,
        "sonar.kotlin.ktlint.reportPaths" to ktLintReportFiles,
        "sonar.junit.reportPaths" to junitReportFiles,
    )

project.extra.apply {
    set("androidLintReportFiles", androidLintReportFiles)
    set("detektReportFiles", detektReportFiles)
    set("jacocoXmlReportFiles", jacocoXmlReportFiles)
    set("junitReportFiles", junitReportFiles)
    set("ktLintReportFiles", ktLintReportFiles)
    set("projectSonarProperties", projectSonarProperties)
}

configure<SonarExtension> {
    properties {
        projectSonarProperties.forEach { (key: String, value: Any) ->
            property(key, value)
            project.logger.debug("SONAR $key $value")
        }
    }
}
