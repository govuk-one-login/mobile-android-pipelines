package uk.gov.pipelines.extensions

import org.gradle.api.Project
import uk.gov.pipelines.config.buildLogicDir
import uk.gov.pipelines.utils.isWindows
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

/**
 * Resolves the Vale executable path from the system PATH.
 *
 * @return The absolute path to the Vale executable
 * @throws IllegalStateException if Vale executable is not found on PATH
 */
internal fun Project.resolveValeExecutable(): String =
    resolveValeExecutable(
        executeCommand = { command ->
            providers.exec {
                commandLine(command)
            }.standardOutput.asText.map { it.trim() }.get()
        },
    )

/**
 * Testable version that accepts a command executor and OS detector.
 *
 * @param isWindowsOs Function to detect if running on Windows
 * @param executeCommand Function to execute system commands, allowing for testing without actual execution
 */
internal fun Project.resolveValeExecutable(
    isWindowsOs: () -> Boolean = ::isWindows,
    executeCommand: (List<String>) -> String,
): String {
    val whichCommand =
        if (isWindowsOs()) {
            listOf("where", "vale")
        } else {
            listOf("which", "vale")
        }

    val result =
        try {
            executeCommand(whichCommand).trim()
        } catch (_: Exception) {
            "" // Return empty string when command fails
        }

    return result.ifEmpty {
        error("Vale executable not found on PATH. Please install Vale or ensure it's available in your PATH.")
    }
}
