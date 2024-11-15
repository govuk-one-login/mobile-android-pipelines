package uk.gov.pipelines.config

import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.io.File

private object RootProjectExtras {
    /**
     * The directory of the `buildLogic` project, within mobile-android-pipelines project,
     * relative to the root of the composite Gradle project.
     */
    const val BUILD_LOGIC_DIR = "buildLogicDir"
}

/**
 * @see [RootProjectExtras.BUILD_LOGIC_DIR]
 */
internal val Project.buildLogicDir: File
    get() = rootDir.resolve(rootProjectExtra(RootProjectExtras.BUILD_LOGIC_DIR))

private fun Project.rootProjectExtra(key: String): String {
    // Gradle may create a temporary project and run this code while
    // generating `accessors` for pre-compiled script plugins.
    // There is little documentation around this but you can find some
    // related discussion at https://github.com/gradle/gradle/issues/15383.
    // The extras aren't available or required during this pass, so just
    // return an empty string.
    if (rootProject.name == "gradle-kotlin-dsl-accessors") {
        return ""
    }

    return (rootProject.extra.get(key) ?: error("Add $key to root project extras"))
        .toString()
}
