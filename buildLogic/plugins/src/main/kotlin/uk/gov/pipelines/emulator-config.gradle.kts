package uk.gov.pipelines

import com.android.build.gradle.BaseExtension
import uk.gov.pipelines.emulator.EmulatorConfig
import uk.gov.pipelines.extensions.BaseExtensions.generateDeviceConfigurations
import uk.gov.pipelines.extensions.BaseExtensions.generateGetHardwareProfilesTask
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.FileReader

plugins {
    id("kotlin-android")
}

val emulatorConfig: EmulatorConfig by rootProject.extra

/**
 * Configure both app and library modules via the [BaseExtension].
 *
 * Generates applicable Android Virtual Device (AVD) configurations via
 * [generateGetHardwareProfilesTask] output. These configuration act as Gradle managed devices
 * within a given Gradle module, generating instrumentation test tasks based on the device profiles
 * made.
 */
configure<BaseExtension> {
    val consoleOutputStream = ByteArrayOutputStream()
    val hardwareProfilesList =
        rootProject.file(
            "${rootProject.buildDir}/outputs/managedDeviceHardwareProfiles.txt",
        )
    val hardwareProfilesTask = generateGetHardwareProfilesTask(project, hardwareProfilesList)

    if (!hardwareProfilesList.exists()) {
        /**
         * Call the hardware profiles task within the gradle configuration stage for the sake of
         * building out the various hardware profiles.
         */
        exec {
            commandLine = hardwareProfilesTask.get().commandLine
            args = hardwareProfilesTask.get().args
            standardOutput = consoleOutputStream
        }
    }

    val hardwareProfileStrings: List<String> =
        BufferedReader(FileReader(hardwareProfilesList))
            .readLines()
            .filter { profile ->
                emulatorConfig.deviceFilters.any { filter ->
                    profile.contains(filter, ignoreCase = true)
                }
            }

    generateDeviceConfigurations(
        androidApiLevels = emulatorConfig.androidApiLevels,
        hardwareProfileStrings = hardwareProfileStrings,
        systemImageSources = emulatorConfig.systemImageSources,
    )
}
