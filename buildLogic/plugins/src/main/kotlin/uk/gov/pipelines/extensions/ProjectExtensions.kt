package uk.gov.pipelines.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.process.ExecSpec

/**
 * Default version code if no `versionCode` property is supplied to the build.
 *
 * Google Play will reject builds with this default value because it is larger than the maximum
 * allowed which is 2100000000.
 */
private const val DEFAULT_VERSION_CODE: Int = Integer.MAX_VALUE

/**
 * Default version name if no `versionName` property is supplied to the build.
 */
private const val DEFAULT_VERSION_NAME: String = "0.1.0"

object ProjectExtensions {
    fun Project.execWithOutput(spec: ExecSpec.() -> Unit): String =
        providers.exec {
            this.spec()
        }.standardOutput.asText.map(String::trim).get()

    /**
     * The version code for the application.
     *
     * The value is sourced from the `versionCode` property if it is provided, otherwise it
     * defaults to [DEFAULT_VERSION_CODE].
     */
    val Project.versionCode
        get() = prop("versionCode") { DEFAULT_VERSION_CODE }.toInt()

    val Project.versionName: String
        get() = prop("versionName") { DEFAULT_VERSION_NAME }

    private fun Project.prop(
        key: String,
        default: () -> Any,
    ): String {
        return providers.gradleProperty(key)
            .orNull.takeUnless { it.isNullOrEmpty() }
            ?: default().toString()
    }

    fun Project.debugLog(messageSuffix: String) {
        logger.debug("${project.path}: $messageSuffix")
    }

    fun Project.infoLog(messageSuffix: String) {
        logger.info("${project.path}: $messageSuffix")
    }

    val Project.libs
        get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
}
