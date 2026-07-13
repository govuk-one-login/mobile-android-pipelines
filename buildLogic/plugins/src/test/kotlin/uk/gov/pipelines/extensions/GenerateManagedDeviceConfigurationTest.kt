package uk.gov.pipelines.extensions

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.pipelines.emulator.SystemImageSource
import uk.gov.pipelines.extensions.BaseExtensions.generateDeviceConfigurations

class GenerateManagedDeviceConfigurationTest {
    private val project = ProjectBuilder.builder().build()

    private val hardwareProfile = "Pixel XL"
    private val apiLevel = 34
    private val systemImageSource = SystemImageSource.AOSP_ATD

    private lateinit var commonExtension: CommonExtension

    @BeforeEach
    fun setUp() {
        project.plugins.apply("com.android.library")

        commonExtension = project.extensions.findByType<CommonExtension>()!!
    }

    @Test
    fun `sets testInstrumentationRunner`() {
        generateDeviceConfigurations()

        val runner = commonExtension.defaultConfig.testInstrumentationRunner

        assertThat(runner, equalTo("androidx.test.runner.AndroidJUnitRunner"))
    }

    @Test
    fun `disables animations`() {
        generateDeviceConfigurations()

        val animationsDisabled = commonExtension.testOptions.animationsDisabled

        assertThat(animationsDisabled, equalTo(true))
    }

    @Test
    fun `sets execution to ANDROIDX_TEST_ORCHESTRATOR`() {
        generateDeviceConfigurations()

        val execution = commonExtension.testOptions.execution

        assertThat(execution, equalTo("androidx_test_orchestrator"))
    }

    @Test
    fun `creates managed device with correct hardware profile`() {
        generateDeviceConfigurations()

        val device = getManagedDevice()

        assertThat(device.device, equalTo(hardwareProfile))
    }

    @Test
    fun `creates managed device with correct API level`() {
        generateDeviceConfigurations()

        val device = getManagedDevice()

        assertThat(device.apiLevel, equalTo(apiLevel))
    }

    @Test
    fun `creates managed device with correct system image source`() {
        generateDeviceConfigurations()

        val device = getManagedDevice()

        assertThat(device.systemImageSource, equalTo(systemImageSource.image))
    }

    private fun generateDeviceConfigurations() {
        commonExtension.generateDeviceConfigurations(
            hardwareProfileStrings = listOf(hardwareProfile),
            androidApiLevels = listOf(apiLevel),
            systemImageSources = listOf(systemImageSource),
        )
    }

    private fun getManagedDevice(): ManagedVirtualDevice {
        val expectedDeviceName = "aospAtdPixelXLApi34"

        return commonExtension
            .testOptions
            .managedDevices
            .allDevices
            .named(expectedDeviceName)
            .get() as ManagedVirtualDevice
    }
}
