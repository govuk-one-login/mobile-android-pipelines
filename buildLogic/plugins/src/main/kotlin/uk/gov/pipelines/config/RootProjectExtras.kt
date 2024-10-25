package uk.gov.pipelines.config

import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.io.File

private object RootProjectExtras {
    /**
     * The directory of the buildLogic project, within mobile-android-pipelines project,
     * relative to the root of the composite Gradle project.
     */
    const val BUILD_LOGIC_DIR = "buildLogicDir"
}

/**
 * @see [RootProjectExtras.BUILD_LOGIC_DIR]
 */
internal val Project.buildLogicDir: File
    get() = rootDir.resolve(rootProjectExtra(RootProjectExtras.BUILD_LOGIC_DIR))

private fun Project.rootProjectExtra(key: String): String =
    (rootProject.extra.get(key) ?: error("Add $key to root project extras"))
        .toString()
