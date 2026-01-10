package uk.gov.pipelines

import org.gradle.api.tasks.Exec
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.pipelines.extras.BUILD_LOGIC_DIR

class ValeConfigPluginTest {
    private val project = ProjectBuilder.builder().build()

    @BeforeEach
    fun setup() {
        project.rootProject.extraProperties.set("buildLogicDir", BUILD_LOGIC_DIR)
    }

    @Test
    fun `vale task passes correct arguments to vale executable`() {
        project.pluginManager.apply("uk.gov.pipelines.vale-config")

        val valeTask = project.tasks.getByName("vale") as Exec
        val projectDir = project.rootProject.projectDir.toString()
        val buildLogicDir = project.rootProject.projectDir.resolve(BUILD_LOGIC_DIR).canonicalPath
        val expectedArgs =
            listOf(
                "--no-wrap",
                "--config=$buildLogicDir/config/vale/.vale.ini",
                "--glob=!**/{build,.gradle,mobile-android-pipelines}/**",
                projectDir,
            )

        assertEquals(expectedArgs, valeTask.args)
    }
}
