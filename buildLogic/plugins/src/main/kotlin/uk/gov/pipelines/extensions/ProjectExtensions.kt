package uk.gov.pipelines.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.process.ExecSpec
import uk.gov.pipelines.config.buildLogicDir
import uk.gov.pipelines.output.OutputStreamGroup
import java.io.ByteArrayOutputStream
import java.io.OutputStream

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

    val Project.versionCode
        get() = prop("versionCode", Integer.MAX_VALUE).toInt()
    val Project.versionName: String
        get() {
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
