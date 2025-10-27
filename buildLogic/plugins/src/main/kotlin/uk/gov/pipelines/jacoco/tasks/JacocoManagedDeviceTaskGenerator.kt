package uk.gov.pipelines.jacoco.tasks

import com.android.build.gradle.internal.tasks.ManagedDeviceInstrumentationTestTask
import org.gradle.api.Project
import uk.gov.pipelines.Filters
import uk.gov.pipelines.extensions.StringExtensions.capitaliseFirstCharacter
import uk.gov.pipelines.filetree.fetcher.FileTreeFetcher
import uk.gov.pipelines.jacoco.config.JacocoManagedDeviceConfig

class JacocoManagedDeviceTaskGenerator(
    private val project: Project,
    private val classDirectoriesFetcher: FileTreeFetcher,
    private val reportDirectoryPrefix: String =
        project.layout.buildDirectory.dir(
            "reports/jacoco",
        ).map { it.asFile.absolutePath }.get(),
) : JacocoTaskGenerator {
    override fun generate() {
        val testTasks: Iterable<ManagedDeviceInstrumentationTestTask> =
            project.tasks.withType(
                ManagedDeviceInstrumentationTestTask::class.java,
            )

        testTasks.map { testTask ->
            val jacocoTaskName = "jacoco${testTask.name.capitaliseFirstCharacter()}Report"
            testTask to
                JacocoManagedDeviceConfig(
                    project = project,
                    classDirectoryFetcher = classDirectoriesFetcher,
                    name = jacocoTaskName,
                    testTaskName = testTask.name,
                )
        }.forEach { (testTask, configuration) ->
            configuration.generateCustomJacocoReport(
                excludes = Filters.androidInstrumentationTests,
                dependencies = listOf(testTask),
                description = "Create coverage report from the '${testTask.name}' instrumentation tests.",
                reportOutputDir = "$reportDirectoryPrefix/managed/${testTask.name}",
            )
        }
    }
}
