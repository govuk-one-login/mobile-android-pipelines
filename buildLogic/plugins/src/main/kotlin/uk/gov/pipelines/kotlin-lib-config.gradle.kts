package uk.gov.pipelines

import uk.gov.pipelines.extensions.ProjectExtensions.versionName

listOf(
    "java-library",
    "java-test-fixtures",
    "org.jetbrains.kotlin.jvm",
    "uk.gov.publishing.config",
).forEach {
    project.plugins.apply(it)
}

version = versionName

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.toVersion(21)
    targetCompatibility = JavaVersion.toVersion(21)
}
