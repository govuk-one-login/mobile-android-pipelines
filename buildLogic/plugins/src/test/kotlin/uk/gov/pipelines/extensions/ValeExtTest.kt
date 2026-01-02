package uk.gov.pipelines.extensions

import org.gradle.api.Project
import org.gradle.internal.extensions.core.extra
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import uk.gov.pipelines.config.buildLogicDir
import java.io.File

class ValeExtTest {
    @TempDir
    lateinit var tempDir: File
    
    private lateinit var project: Project
    
    @BeforeEach
    fun setUp() {
        project = ProjectBuilder.builder()
            .withProjectDir(tempDir)
            .build()
        project.rootProject.extra.set("buildLogicDir", "buildLogic")
    }

    @Test
    fun `valeConfigFile returns override file when it exists`() {
        // Given
        val overrideFile = project.file("${project.rootProject.rootDir}/.vale.ini")

        // When
        val result = project.valeConfigFile { file -> file == overrideFile }

        // Then
        assertEquals(overrideFile, result)
    }

    @Test
    fun `valeConfigFile returns default file when override does not exist`() {
        // Given
        val expectedDefault = project.file("${project.rootProject.buildLogicDir}/config/vale/.vale.ini")

        // When
        val result = project.valeConfigFile { false }

        // Then
        assertEquals(expectedDefault, result)
    }

    @Test
    fun `valeConfigFile with default behavior uses file exists check`() {
        // Given
        val overrideFile = File(project.rootProject.rootDir, ".vale.ini")
        overrideFile.createNewFile()

        // When
        val result = project.valeConfigFile()

        // Then
        assertEquals(overrideFile, result)
    }
}
