package uk.gov.pipelines.extensions

import org.gradle.api.Project
import uk.gov.pipelines.config.buildLogicDir
import java.io.File

/**
 * Get the custom vale configuration file defined by the project.
 * If a custom vale configuration doesn't exist, get the default file.
 */
internal fun Project.valeConfigFile(): File = valeConfigFile { it.exists() }

internal fun Project.valeConfigFile(fileExists: (File) -> Boolean): File {
    val overrideFile = file("${rootProject.rootDir}/.vale.ini")

    if (fileExists(overrideFile)) {
        return overrideFile
    }

    return file("${rootProject.buildLogicDir}/config/vale/.vale.ini")
}
