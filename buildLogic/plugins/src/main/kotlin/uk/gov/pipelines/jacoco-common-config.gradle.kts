package uk.gov.pipelines

import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import uk.gov.pipelines.extensions.ProjectExtensions.debugLog
import uk.gov.pipelines.extensions.TestExt.decorateTestTasksWithJacoco
import uk.gov.pipelines.plugins.BuildConfig

project.plugins.apply("jacoco")

project.configure<JacocoPluginExtension> {
    this.toolVersion = BuildConfig.JACOCO_TOOL_VERSION
    project.logger.debug("Applied jacoco tool version to jacoco plugin")
}

project.tasks.withType<Test> {
    decorateTestTasksWithJacoco().also {
        project.debugLog("Applied jacoco properties to Test tasks")
    }
}
