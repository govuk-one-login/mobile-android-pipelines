package uk.gov.pipelines.extensions

import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.pipelines.extensions.ProjectExtensions.versionName
import uk.gov.pipelines.extras.BUILD_LOGIC_DIR

class ProjectExtensionsTest {
    private val project = ProjectBuilder.builder().build()

    @BeforeEach
    fun setup() {
        project.rootProject.extraProperties.set(
            "buildLogicDir",
            BUILD_LOGIC_DIR,
        )
    }

    @Test
    fun `default version name`() {
        assertEquals("0.1.0", project.versionName)
    }
}
