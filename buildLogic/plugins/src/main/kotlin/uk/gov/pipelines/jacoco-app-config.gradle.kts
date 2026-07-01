package uk.gov.pipelines

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.internal.coverage.JacocoReportTask
import com.android.build.gradle.internal.tasks.ManagedDeviceInstrumentationTestTask
import uk.gov.pipelines.extensions.LibraryExtensionExt.decorateExtensionWithJacoco
import uk.gov.pipelines.extensions.ProjectExtensions.debugLog
import uk.gov.pipelines.extensions.StringExtensions.capitaliseFirstCharacter
import uk.gov.pipelines.extensions.generateDebugJacocoTasks

project.plugins.apply("uk.gov.pipelines.jacoco-common-config")

project.configure<ApplicationExtension> {
    this.decorateExtensionWithJacoco().also {
        project.debugLog("Applied jacoco properties to application")
    }
}

/**
 * Configure managed device test tasks to also run the managed device coverage report task created
 * by Google.
 */
project.configure<ApplicationAndroidComponentsExtension> {
    onVariants(selector().withBuildType("debug")) { variant ->
        project.afterEvaluate {
            this.tasks.withType<ManagedDeviceInstrumentationTestTask>().filter {
                it.name.contains(variant.name, ignoreCase = true)
            }.forEach { instrumentationTestTask ->
                val coverageReportTaskName =
                    "createManagedDevice${variant.name.capitaliseFirstCharacter()}AndroidTestCoverageReport"
                val androidCoverageReportTask =
                    project.tasks.named(
                        coverageReportTaskName,
                        JacocoReportTask::class.java,
                    )

                instrumentationTestTask.finalizedBy(androidCoverageReportTask)
                androidCoverageReportTask.configure {
                    this.mustRunAfter(instrumentationTestTask)
                }
            }
        }
    }

    finalizeDsl {
        it.decorateExtensionWithJacoco().also {
            project.debugLog("Applied jacoco properties to app")
        }
    }
}

project.configure<ApplicationAndroidComponentsExtension> {
    onVariants(selector().withBuildType("debug")) { variant ->
        variant.generateDebugJacocoTasks(project)
    }
}
