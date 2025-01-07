package uk.gov.pipelines.extensions

import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.matchesPattern
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.pipelines.extensions.ProjectExtensions.versionName
import uk.gov.pipelines.extras.BUILD_LOGIC_DIR

class ProjectExtensionsTest {
    private val project = ProjectBuilder.builder().build()

    companion object {
        private const val VERSION_PATTERN = """[0-9]\.[0-9]\.[0-9]"""
    }

    @BeforeEach
    fun setup() {
        project.rootProject.extraProperties.set(
            "buildLogicDir",
            BUILD_LOGIC_DIR,
        )
    }

    @Test
    fun `version name`() {
        assertThat(project.versionName, matchesPattern(VERSION_PATTERN))
    }
}
