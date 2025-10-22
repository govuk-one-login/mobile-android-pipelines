package uk.gov.pipelines

import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import uk.gov.pipelines.EmulatorConfigProjectExtensions.setupEmulatorConfigExtras
import uk.gov.pipelines.ManagedDevicesMatchers.hasDevice
import uk.gov.pipelines.TestOptionsMatchers.hasManagedDevices

class EmulatorConfigPluginTest {
    private val project = ProjectBuilder.builder().build()

    @BeforeEach
    fun setUp() {
        project.setupEmulatorConfigExtras()

        listOf(
            "com.android.library",
            "uk.gov.pipelines.emulator-config",
        ).forEach(project.pluginManager::apply)
    }

    @Test
    fun `Plugin creates a device based on EmulatorConfig`() {
        project.extensions.findByType<LibraryExtension>()?.let { extension ->
            assertThat(
                extension.testOptions,
                hasManagedDevices(
                    hasDevice("aospAtdPixelXLApi34"),
                ),
            )
        } ?: fail { "Cannot find android library extension to verify managed devices!" }
    }
}
