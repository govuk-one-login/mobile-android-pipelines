package uk.gov.pipelines

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import uk.gov.pipelines.emulator.EmulatorConfig
import uk.gov.pipelines.emulator.SystemImageSource
import uk.gov.pipelines.extras.BUILD_LOGIC_DIR

object EmulatorConfigProjectExtensions {
    val exampleEmulatorConfig =
        EmulatorConfig(
            systemImageSources = setOf(SystemImageSource.AOSP_ATD),
            androidApiLevels = setOf(34),
            deviceFilters = setOf("Pixel XL"),
        )
    val exampleManagedDeviceName = "aospAtdPixelXLApi34"

    fun Project.setupEmulatorConfigExtras(
        buildLogicDir: String = BUILD_LOGIC_DIR,
        emulatorConfig: EmulatorConfig = exampleEmulatorConfig,
    ) {
        project.rootProject.extraProperties.set(
            "buildLogicDir",
            buildLogicDir,
        )
        project.rootProject.extraProperties.set(
            "emulatorConfig",
            emulatorConfig,
        )
    }
}
