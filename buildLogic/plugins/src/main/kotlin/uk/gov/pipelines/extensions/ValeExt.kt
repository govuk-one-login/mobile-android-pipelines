package uk.gov.pipelines.extensions

import org.gradle.api.Project
import uk.gov.pipelines.config.buildLogicDir
import java.io.File

/**
 * Get the custom vale configuration file defined by the project.
 * If a custom vale configuration does not exist, get the default file.
 */
internal fun Project.valeConfigFile(): File {
    val overrideFile = file("${rootProject.projectDir}/.vale.ini")

    if (overrideFile.exists()) {
        return overrideFile
    }

    return file("${rootProject.buildLogicDir}/config/vale/.vale.ini")
}
