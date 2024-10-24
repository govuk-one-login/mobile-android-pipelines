package uk.gov.pipelines.config

import org.gradle.api.Project

object GradleProperties {
    private const val NAMESPACE = "uk.gov.pipelines"

    /**
     * The directory of the mobile-android-pipelines project.
     */
    const val DIR = "$NAMESPACE.dir"

    fun Project.rootProjectGradleProperty(key: String): String =
        rootProject.providers.gradleProperty(key).orNull
            ?: error("Add $key to gradle.properties")
}