package uk.gov.pipelines.utils

/**
 * Checks if the current operating system is Windows.
 *
 * @return true if running on Windows, false otherwise
 */
internal fun isWindows(): Boolean = System.getProperty("os.name").lowercase().contains("windows")
