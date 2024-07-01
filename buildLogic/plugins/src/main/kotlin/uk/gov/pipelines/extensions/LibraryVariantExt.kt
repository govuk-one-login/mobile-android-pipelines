package uk.gov.pipelines.extensions

import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.LibraryVariant
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import uk.gov.pipelines.filetree.fetcher.FileTreesFetcher
import uk.gov.pipelines.filetree.fetcher.JavaCompileFileTreeFetcher
import uk.gov.pipelines.filetree.fetcher.KotlinCompileFileTreeFetcher
import uk.gov.pipelines.jacoco.tasks.JacocoConnectedTestTaskGenerator
import uk.gov.pipelines.jacoco.tasks.JacocoManagedDeviceTaskGenerator
import uk.gov.pipelines.jacoco.tasks.JacocoTaskGenerator
import uk.gov.pipelines.jacoco.tasks.JacocoUnitTestTaskGenerator

fun LibraryVariant.generateDebugJacocoTasks(
    project: Project
) {
    val capitalisedFlavorName = flavorName?.capitalized() ?: error(
        "The library variant has no flavor name!"
    )
    generateJacocoTasks(
        project,
        buildType.name,
        name,
        capitalisedFlavorName
    )
}

fun ApplicationVariant.generateDebugJacocoTasks(
    project: Project
) {
    val capitalisedFlavorName = flavorName?.capitalized() ?: error(
        "The application variant has no flavor name!"
    )
    generateJacocoTasks(
        project,
        buildType.name,
        name,
        capitalisedFlavorName
    )
}

private fun generateJacocoTasks(
    project: Project,
    buildType: String?,
    name: String,
    capitalisedFlavorName: String
) {
    if (buildType == "debug") {
        val classDirectoriesFetcher = generateClassDirectoriesFetcher(
            project,
            name,
            capitalisedFlavorName
        )

        val unitTestReportGenerator = JacocoUnitTestTaskGenerator(
            project,
            classDirectoriesFetcher,
            name
        )

        val connectedTestReportGenerator = JacocoConnectedTestTaskGenerator(
            project,
            classDirectoriesFetcher,
            name
        )

        val managedDeviceTestReportGenerator = JacocoManagedDeviceTaskGenerator(
            project,
            classDirectoriesFetcher
        )

        listOf(
            unitTestReportGenerator,
            connectedTestReportGenerator,
            managedDeviceTestReportGenerator
        ).forEach(JacocoTaskGenerator::generate)
    }
}

private fun generateClassDirectoriesFetcher(
    project: Project,
    name: String,
    capitalisedFlavorName: String
): FileTreesFetcher = FileTreesFetcher(
    project,
    KotlinCompileFileTreeFetcher(
        project,
        name,
        capitalisedFlavorName
    ),
//    AsmFileTreeFetcher(
//        project,
//        name,
//        capitalisedFlavorName,
//    ),
    JavaCompileFileTreeFetcher(
        project,
        name,
        capitalisedFlavorName
    )
)
