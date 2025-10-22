package uk.gov.pipelines

import com.android.build.gradle.BaseExtension
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import uk.gov.pipelines.EmulatorConfigProjectExtensions.exampleEmulatorConfig
import uk.gov.pipelines.EmulatorConfigProjectExtensions.setupEmulatorConfigExtras
import uk.gov.pipelines.ManagedDevicesMatchers.hasSize
import uk.gov.pipelines.TestOptionsMatchers.hasManagedDevices

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

        project.extensions.findByType<BaseExtension>()?.let { extension ->
            assertThat(
                extension.testOptions,
                hasManagedDevices(hasSize(1)),
            )
        } ?: fail { "Cannot find android library extension to verify managed devices!" }
    }
}
