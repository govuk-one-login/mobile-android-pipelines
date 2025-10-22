package uk.gov.pipelines.jacoco.tasks

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.FieldSource
import uk.gov.pipelines.EmulatorConfigProjectExtensions.setupEmulatorConfigExtras
import uk.gov.pipelines.filetree.fetcher.FileTreeFetcher

class JacocoTaskGeneratorTest {
    @ParameterizedTest
    @FieldSource("generators")
    fun `Standard task generators create applicable JacocoReport task`(
        generator: JacocoTaskGenerator,
        expectedTaskName: String,
    ) {
        generator.generate()

        // Throws an exception if the task cannot be found
        project.tasks.named(expectedTaskName)
    }

    /**
     * Due to the internal use of task finding within the [JacocoManagedDeviceTaskGenerator],
     * calling [JacocoTaskGenerator.generate] doesn't create any additional tasks.
     */
    @Test
    fun `Can create a task generator for Gradle managed devices`() {
        project.setupEmulatorConfigExtras()

        listOf(
            "com.android.library",
            "uk.gov.pipelines.emulator-config",
        ).forEach(project.pluginManager::apply)

        assertNotNull(
            JacocoManagedDeviceTaskGenerator(
                project = project,
                classDirectoriesFetcher = fetcher,
            ),
        )
    }

    companion object {
        private val project = ProjectBuilder.builder().build()
        private val fetcher = FileTreeFetcher { project.provider { project.fileTree("./") } }

        @JvmStatic
        val generators =
            listOf(
                arguments(
                    JacocoConnectedTestTaskGenerator(
                        project = project,
                        classDirectoriesFetcher = fetcher,
                        variant = "debug",
                    ),
                    "jacocoDebugConnectedTestReport",
                ),
                arguments(
                    JacocoUnitTestTaskGenerator(
                        project = project,
                        classDirectoriesFetcher = fetcher,
                        variant = "debug",
                    ),
                    "jacocoDebugUnitTestReport",
                ),
            )
    }
}
