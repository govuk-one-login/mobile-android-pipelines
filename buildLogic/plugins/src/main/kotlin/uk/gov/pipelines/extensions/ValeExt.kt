package uk.gov.pipelines.extensions

import org.gradle.api.Project
import uk.gov.pipelines.config.buildLogicDir
import java.io.File

/**
 * Get the Vale configuration file for the project.
 *
 * First checks for a custom .vale.ini file in the root directory.
 * If not found, returns the default configuration from buildLogic/config/vale/.vale.ini
 */
internal fun Project.valeConfigFile(): File = valeConfigFile { it.exists() }

/**
 * Testable version that accepts a file existence checker.
 *
 * @param fileExists Function to check if a file exists, allowing for testing without file system dependencies
 */
internal fun Project.valeConfigFile(fileExists: (File) -> Boolean): File {
    val overrideFile = file("${rootProject.rootDir}/.vale.ini")

    if (fileExists(overrideFile)) {
        return overrideFile
    }

    return file("${rootProject.buildLogicDir}/config/vale/.vale.ini")
}
