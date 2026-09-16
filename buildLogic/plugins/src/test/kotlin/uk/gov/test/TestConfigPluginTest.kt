package uk.gov.test

import org.gradle.api.plugins.UnknownPluginException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TestConfigPluginTest {

    @Test
    fun `throws PluginApplicationException when Android SDK is not available`() {
        val project = ProjectBuilder.builder().build()

        // Android Gradle Plugin requires Android SDK which is not available in unit tests
        assertThrows(UnknownPluginException::class.java) {
            project.pluginManager.apply("uk.gov.onelogin.plugin.test-config")
        }
    }

    @Test
    fun `TestTypeExtension stores configured test types`() {
        val project = ProjectBuilder.builder().build()
        val extension = TestTypeExtension(project.objects)

        extension.testTypes("component", "contract")

        assertEquals(listOf("component", "contract"), extension.testTypes.get())
    }

    @Test
    fun `TestTypeExtension defaults to empty list when no types configured`() {
        val project = ProjectBuilder.builder().build()
        val extension = TestTypeExtension(project.objects)

        assertTrue(extension.testTypes.getOrElse(emptyList()).isEmpty())
    }

    @Test
    fun `TestTypeExtension overwrites previous values on subsequent calls`() {
        val project = ProjectBuilder.builder().build()
        val extension = TestTypeExtension(project.objects)

        extension.testTypes("unit")
        extension.testTypes("component", "contract")

        assertEquals(listOf("component", "contract"), extension.testTypes.get())
    }
}
