package uk.gov.pipelines

import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import uk.gov.pipelines.publishing.MavenPublishingConfigProjectExtensions.configurePublishingPlugin

class AndroidLibConfigPluginTest {
    @Test
    fun `throws PluginApplicationException when Android SDK is not available`() {
        val project = ProjectBuilder.builder().build()
        project.configurePublishingPlugin()

        // Android Gradle Plugin requires Android SDK which is not available in unit tests
        assertThrows(PluginApplicationException::class.java) {
            project.pluginManager.apply("uk.gov.pipelines.android-lib-config")
        }
    }
}
