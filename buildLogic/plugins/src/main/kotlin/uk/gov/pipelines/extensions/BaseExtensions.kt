package uk.gov.pipelines.extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.internal.tasks.ManagedDeviceInstrumentationTestTask
import com.android.build.gradle.internal.tasks.ManagedDeviceSetupTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.maybeCreate
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import uk.gov.pipelines.config.ApkConfig
import uk.gov.pipelines.config.buildLogicDir
import uk.gov.pipelines.emulator.SystemImageSource
import uk.gov.pipelines.extensions.ProjectExtensions.versionCode
import uk.gov.pipelines.extensions.ProjectExtensions.versionName
import uk.gov.pipelines.extensions.StringExtensions.capitaliseFirstCharacter
import uk.gov.pipelines.extensions.StringExtensions.proseToUpperCamelCase

object BaseExtensions {
    private val filter = Regex("[/\\\\:<>\"?*| ()]")

    /**
     * Registers a task that defers to the `getAllHardwareProfileNames` script found within the
     * `scripts/` folder.
     *
     * Outputs all applicable hardware profiles available on the machine running this task.
     */
    internal fun generateGetHardwareProfilesTask(
        project: Project,
        hardwareProfilesOutput: Provider<RegularFile>,
    ) = project.tasks.register("getHardwareProfiles", Exec::class) {
        commandLine(
            "bash",
            "${project.buildLogicDir}/scripts/getAllHardwareProfileNames",
            hardwareProfilesOutput.map { it.asFile.absolutePath }.get(),
        )
        onlyIf("The output file doesn't exist") {
            !hardwareProfilesOutput.map { it.asFile.exists() }.get()
        }
    }

    /**
     * Creates a Gradle managed device within the associated Gradle project.
     *
     * This effectively creates a [ManagedDeviceSetupTask] to create, build and verify the initial
     * state of a new emulator. There is also a [ManagedDeviceInstrumentationTestTask] created,
     * respecting `${flavor}${buildType}AndroidTest` naming conventions.
     */
    private fun CommonExtension.generateManagedDeviceConfiguration(
        hardwareProfile: String,
        apiLevel: Int,
        source: SystemImageSource,
    ) {
        val managedDeviceName = generateDeviceName(hardwareProfile, source, apiLevel)

        defaultConfig.apply {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        testOptions.apply {
            animationsDisabled = true
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
            managedDevices.apply {
                allDevices.maybeCreate<ManagedVirtualDevice>(
                    managedDeviceName,
                ).apply {
                    // Use device profiles you typically see in Android Studio.
                    this.device = hardwareProfile
                    // Use only API levels 27 and higher.
                    this.apiLevel = apiLevel
                    // To include Google services, use "google"
                    this.systemImageSource = source.image
                }
            }
        }
    }

    private fun generateDeviceName(
        hardwareProfile: String,
        source: SystemImageSource,
        apiLevel: Int,
    ): String {
        val hardwareProfileTaskSegment =
            hardwareProfile.replace(
                filter,
                "",
            ).proseToUpperCamelCase()

        val systemImageSourceTaskSegment = source.sanitise()

        return systemImageSourceTaskSegment +
            "${hardwareProfileTaskSegment.capitaliseFirstCharacter()}Api$apiLevel"
    }

    /**
     * Loops through the provided parameters, deferring each entry to the
     * [generateManagedDeviceConfiguration] function.
     */
    fun CommonExtension.generateDeviceConfigurations(
        hardwareProfileStrings: Collection<String>,
        androidApiLevels: Collection<Int>,
        systemImageSources: Collection<SystemImageSource> = SystemImageSource.values().asList(),
    ) {
        hardwareProfileStrings.forEach { hardwareProfileString ->
            androidApiLevels.forEach { apiLevel ->
                systemImageSources.forEach { systemImageSource ->
                    generateManagedDeviceConfiguration(
                        hardwareProfile = hardwareProfileString,
                        apiLevel = apiLevel,
                        source = systemImageSource,
                    )
                }
            }
        }
    }

    fun ApplicationExtension.baseAndroidConfig(project: Project) {
        commonBaseAndroidConfig(project)

        val apkConfig: ApkConfig by project.rootProject.extra
        defaultConfig.apply {
            targetSdk = apkConfig.sdkVersions.target
            versionCode = project.versionCode
            versionName = project.versionName
        }
    }

    fun LibraryExtension.baseAndroidConfig(project: Project) = commonBaseAndroidConfig(project)

    private fun CommonExtension.commonBaseAndroidConfig(project: Project) {
        val apkConfig: ApkConfig by project.rootProject.extra

        compileSdk = apkConfig.sdkVersions.compile
        defaultConfig.apply {
            minSdk = apkConfig.sdkVersions.minimum
            testOptions.apply {
                unitTests.apply {
                    isIncludeAndroidResources = true
                }
            }
        }

        packaging.apply {
            resources.excludes += "META-INF/LICENSE-LGPL-2.1.txt"
            resources.excludes += "META-INF/LICENSE-LGPL-3.txt"
            resources.excludes += "META-INF/LICENSE-W3C-TEST"
            resources.excludes += "META-INF/DEPENDENCIES"
            resources.excludes += "*.proto"
            resources.excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}
