package uk.gov.pipelines.environment

import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class EnvironmentConfigPluginTest {
    private val project = ProjectBuilder.builder().build()

    companion object {
        const val PLUGIN_ID = "uk.gov.pipelines.environment.environment-config"
    }

    @Test
    fun `it creates the environment extension`() {
        project.pluginManager.apply(PLUGIN_ID)

        assertNotNull(project.extensions.findByType<EnvironmentExtension>())
    }

    @Test
    fun `given extension already exists, it doesn't create a new environment extension`() {
        val preexistingExtension =
            project.extensions.create<EnvironmentExtension>("preexistingExtension")

        project.pluginManager.apply(PLUGIN_ID)

        assertNull(project.extensions.findByName(EnvironmentExtension.NAME))

        assertEquals(
            preexistingExtension,
            project.extensions.findByType<EnvironmentExtension>(),
        )
    }
}
