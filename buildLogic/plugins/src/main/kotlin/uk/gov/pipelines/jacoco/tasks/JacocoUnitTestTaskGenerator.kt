package uk.gov.pipelines.jacoco.tasks

import org.gradle.api.Project
import uk.gov.pipelines.Filters
import uk.gov.pipelines.extensions.StringExtensions.capitaliseFirstCharacter
import uk.gov.pipelines.filetree.fetcher.FileTreeFetcher
import uk.gov.pipelines.jacoco.config.JacocoCustomConfig
import uk.gov.pipelines.jacoco.config.JacocoUnitTestConfig

/**
 * A [JacocoTaskGenerator] implementation specifically for unit tests.
 *
 * @param project The Gradle [Project] that houses the generated Jacoco task. Used to generate the
 * relevant [JacocoCustomConfig] instance.
 * @param classDirectoryFetcher The [FileTreeFetcher] that provides the class directories used for
 * reporting code coverage through Jacoco.
 * @param variant The name of the build variant. Used as a parameter to obtain relevant Gradle
 * tasks.
 */
class JacocoUnitTestTaskGenerator(
    private val project: Project,
    private val classDirectoriesFetcher: FileTreeFetcher,
    variant: String,
    private val name: String = "jacoco${variant.capitaliseFirstCharacter()}UnitTestReport",
    configuration: JacocoCustomConfig =
        JacocoUnitTestConfig(
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
        "create${capitalisedVariantName}UnitTestCoverageReport"
    override val description =
        "Create coverage report from the '$capitalisedVariantName' unit tests."
    override val reportsBaseDirectory: String get() = "$reportsDirectoryPrefix/unit"
    override val testTaskName: String get() = "test${capitalisedVariantName}UnitTest"
    override val excludes: List<String> = Filters.androidUnitTests
}
