package uk.gov.pipelines

import com.android.build.api.dsl.CommonExtension
import uk.gov.pipelines.emulator.EmulatorConfig
import uk.gov.pipelines.extensions.BaseExtensions.generateDeviceConfigurations
import uk.gov.pipelines.extensions.BaseExtensions.generateGetHardwareProfilesTask

val emulatorConfig: EmulatorConfig by rootProject.extra

/**
 * Configure both app and library modules via the android extension.
 *
 * Generates applicable Android Virtual Device (AVD) configurations via
 * [generateGetHardwareProfilesTask] output. These configuration act as Gradle managed devices
 * within a given Gradle module, generating instrumentation test tasks based on the device profiles
 * made.
 */
configure<CommonExtension> {
    generateDeviceConfigurations(
        androidApiLevels = emulatorConfig.androidApiLevels,
        hardwareProfileStrings = emulatorConfig.deviceFilters,
        systemImageSources = emulatorConfig.systemImageSources,
    )
}
