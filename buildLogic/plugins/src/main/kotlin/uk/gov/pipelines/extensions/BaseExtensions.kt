package uk.gov.pipelines.extensions

import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.tasks.ManagedDeviceInstrumentationTestTask
import com.android.build.gradle.internal.tasks.ManagedDeviceSetupTask
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.invoke
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
import java.io.File

object BaseExtensions {
    private val filter = Regex("[/\\\\:<>\"?*| ()]")

    /**
     * Registers a task that defers to the `getAllHardwareProfileNames` script found within the
     * `scripts/` folder.
     *
     * Outputs all applicable hardware profiles available on the machine running this task.
     */
    fun BaseExtension.generateGetHardwareProfilesTask(
        project: Project,
        hardwareProfilesOutput: File,
    ) = project.tasks.register("getHardwareProfiles", Exec::class) {
        commandLine(
            "bash",
            "${project.buildLogicDir}/scripts/getAllHardwareProfileNames",
            hardwareProfilesOutput.absolutePath,
        )
        onlyIf("The output file doesn't exist") {
            !hardwareProfilesOutput.exists()
        }
    }

    /**
     * Creates a Gradle managed device within the associated Gradle project.
     *
     * This effectively creates a [ManagedDeviceSetupTask] to create, build and verify the initial
     * state of a new emulator. There is also a [ManagedDeviceInstrumentationTestTask] created,
     * respecting `${flavor}${buildType}AndroidTest` naming conventions.
     */
    private fun BaseExtension.generateManagedDeviceConfiguration(
        hardwareProfile: String,
        apiLevel: Int,
        source: SystemImageSource,
    ) {
        val managedDeviceName = generateDeviceName(hardwareProfile, source, apiLevel)

        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        testOptions {
            animationsDisabled = true
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
            managedDevices {
                devices {
                    maybeCreate<ManagedVirtualDevice>(
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
    fun BaseExtension.generateDeviceConfigurations(
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

    fun BaseExtension.baseAndroidConfig(project: Project) {
        val apkConfig: ApkConfig by project.rootProject.extra

        compileSdkVersion(apkConfig.sdkVersions.compile)
        defaultConfig {
            minSdk = apkConfig.sdkVersions.minimum
            targetSdk = apkConfig.sdkVersions.target
            versionCode = project.versionCode
            versionName = project.versionName

            consumerProguardFiles(
                "consumer-rules.pro",
            )

            packagingOptions {
                resources.excludes += "META-INF/LICENSE-LGPL-2.1.txt"
                resources.excludes += "META-INF/LICENSE-LGPL-3.txt"
                resources.excludes += "META-INF/LICENSE-W3C-TEST"
                resources.excludes += "META-INF/DEPENDENCIES"
                resources.excludes += "*.proto"
                resources.excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
            }

            testOptions {
                unitTests {
                    isIncludeAndroidResources = true
                }
            }
        }
    }
}
