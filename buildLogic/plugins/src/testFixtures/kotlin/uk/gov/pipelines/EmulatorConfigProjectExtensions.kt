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

    fun Project.setupEmulatorConfigExtras(
        buildLogicDir: String = BUILD_LOGIC_DIR,
        emulatorConfig: EmulatorConfig = exampleEmulatorConfig,
    ) {
        project.rootProject.extraProperties.set(
            "buildLogicDir",
            BUILD_LOGIC_DIR,
        )
        project.rootProject.extraProperties.set(
            "emulatorConfig",
            EmulatorConfig(
                systemImageSources = setOf(SystemImageSource.AOSP_ATD),
                androidApiLevels = setOf(34),
                deviceFilters = setOf("Pixel XL"),
            ),
        )
    }
}
