package uk.gov.pipelines

import com.android.build.gradle.BaseExtension
import org.gradle.api.UnknownDomainObjectException
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import uk.gov.pipelines.EmulatorConfigProjectExtensions.exampleEmulatorConfig
import uk.gov.pipelines.EmulatorConfigProjectExtensions.exampleManagedDeviceName
import uk.gov.pipelines.EmulatorConfigProjectExtensions.setupEmulatorConfigExtras

class EmulatorConfigPluginTest {
    private val project = ProjectBuilder.builder().build()

    @BeforeEach
    fun setUp() {
        project.setupEmulatorConfigExtras(
            emulatorConfig = exampleEmulatorConfig,
        )
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "com.android.application",
            "com.android.library",
        ],
    )
    fun `Plugin creates a device based on EmulatorConfig`(androidPlugin: String) {
        listOf(
            androidPlugin,
            "uk.gov.pipelines.emulator-config",
        ).forEach(project.pluginManager::apply)

        // Throws an exception if the name cannot be found
        project.extensions.findByType<BaseExtension>()
            ?.testOptions
            ?.managedDevices
            ?.devices
            ?.named(exampleManagedDeviceName)
            ?: fail { "Cannot find managed device configuration" }
    }

    @Test
    fun `Non existent managed devices throw exceptions`() {
        listOf(
            "com.android.library",
            "uk.gov.pipelines.emulator-config",
        ).forEach(project.pluginManager::apply)

        val deviceName = "unknownDevice"
        val exception: UnknownDomainObjectException =
            assertThrows {
                project.extensions.findByType<BaseExtension>()
                    ?.testOptions
                    ?.managedDevices
                    ?.devices
                    ?.named(deviceName)
            }

        assertEquals(
            "Device with name '$deviceName' not found.",
            exception.message,
        )
    }
}
