package uk.gov.pipelines.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.mockito.Spy
import org.gradle.process.ExecSpec
import uk.gov.pipelines.config.buildLogicDir
import uk.gov.pipelines.output.OutputStreamGroup
import java.io.ByteArrayOutputStream
import java.io.OutputStream

/**
 * Default version code if no `versionCode` property is supplied to the build.
 *
 * Google Play will reject builds with this default value because it is larger than the maximum
 * allowed which is 2100000000.
 */
private const val DEFAULT_VERSION_CODE: Int = Integer.MAX_VALUE

object ProjectExtensions {
    fun Project.execWithOutput(spec: ExecSpec.() -> Unit) =
        OutputStreamGroup().use { outputStreamGroup ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            outputStreamGroup.add(byteArrayOutputStream)
            exec {
                this.spec()
                outputStreamGroup.add(this.standardOutput)
                this.standardOutput = outputStreamGroup
            }
            byteArrayOutputStream.toString().trim()
        }

    /**
     * The version code for the application.
     *
     * The value is sourced from the `versionCode` property if it is provided, otherwise it
     * defaults to [DEFAULT_VERSION_CODE].
     */
    val Project.versionCode
        get() = prop("versionCode", DEFAULT_VERSION_CODE).toInt()

    val Project.versionName: String
        get() {
//            return "0.1.0"
            val scriptsDir = buildLogicDir.resolve("scripts/")
            return execWithOutput {
                workingDir = rootDir
                executable = "bash"
                standardOutput = OutputStream.nullOutputStream()
                args =
                    listOf(
                        scriptsDir.resolve("getCurrentVersion").path,
                    )
            }
        }

    private fun Project.prop(
        key: String,
        default: Any,
    ): String {
        return providers.gradleProperty(key).getOrElse(default.toString())
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
