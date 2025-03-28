package uk.gov.pipelines.jacoco.tasks

import org.gradle.api.Project
import uk.gov.pipelines.Filters
import uk.gov.pipelines.extensions.StringExtensions.capitaliseFirstCharacter
import uk.gov.pipelines.filetree.fetcher.FileTreeFetcher
import uk.gov.pipelines.jacoco.config.JacocoConnectedTestConfig
import uk.gov.pipelines.jacoco.config.JacocoCustomConfig

/**
 * A [JacocoTaskGenerator] implementation specifically for instrumentation tests.
 *
 * @param project The Gradle [Project] that houses the generated Jacoco task. Used to generate the
 * relevant [JacocoCustomConfig] instance.
 * @param classDirectoriesFetcher The [FileTreeFetcher] that provides the class directories used for
 * reporting code coverage through Jacoco.
 * @param variant The name of the build variant. Used as a parameter to obtain relevant Gradle
 * tasks.
 */
class JacocoConnectedTestTaskGenerator(
    private val project: Project,
    private val classDirectoriesFetcher: FileTreeFetcher,
    variant: String,
    name: String = "jacoco${variant.capitaliseFirstCharacter()}ConnectedTestReport",
    configuration: JacocoCustomConfig =
        JacocoConnectedTestConfig(
            project,
            classDirectoriesFetcher,
            variant.capitaliseFirstCharacter(),
            name,
        ),
) : BaseJacocoTaskGenerator(
        project,
        variant,
        configuration,
    ) {
    override val androidCoverageTaskName: String =
        "create${capitalisedVariantName}AndroidTestCoverageReport"
    override val description =
        "Create coverage report from the '$capitalisedVariantName' instrumentation tests."
    override val reportsBaseDirectory: String get() = "$reportsDirectoryPrefix/connected"
    override val testTaskName: String get() = "connected${capitalisedVariantName}AndroidTest"
    override val excludes: List<String> = Filters.androidInstrumentationTests
}
