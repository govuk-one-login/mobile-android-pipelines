package uk.gov.pipelines

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import uk.gov.pipelines.publishing.MavenPublishingConfigProjectExtensions.configurePublishingPlugin

class KotlinLibConfigPluginTest {
    @Test
    fun `Applies an explicit number of plugins`() {
        project.configurePublishingPlugin()
        project.pluginManager.apply("uk.gov.pipelines.kotlin-lib-config")

        assertEquals(CURRENTLY_APPLIED_PLUGINS, project.plugins.size)
    }

    companion object {
        private const val CURRENTLY_APPLIED_PLUGINS = 18
        private val project = ProjectBuilder.builder().build()
    }
}
