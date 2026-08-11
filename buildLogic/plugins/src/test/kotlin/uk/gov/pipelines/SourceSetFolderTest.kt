package uk.gov.pipelines

import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

class SourceSetFolderTest {
    @TempDir
    lateinit var tempDir: File

    private lateinit var sourceSetFolder: SourceSetFolder

    @BeforeEach
    fun setUp() {
        val project = ProjectBuilder.builder().withProjectDir(tempDir).build()
        sourceSetFolder = SourceSetFolder(project)
    }

    @Test
    fun `srcExists returns false when src directory does not exist`() {
        val result = sourceSetFolder.srcExists()

        assertThat(result, equalTo(false))
    }

    @Test
    fun `srcExists returns true when src directory exists`() {
        File(tempDir, "src").mkdir()

        val result = sourceSetFolder.srcExists()

        assertThat(result, equalTo(true))
    }

    @Test
    fun `sourceFolders returns empty string when src does not exist`() {
        val result = sourceSetFolder.sourceFolders

        assertThat(result, equalTo(""))
    }

    @Test
    fun `sourceFolders returns main folder path`() {
        val srcDir = File(tempDir, "src")
        srcDir.mkdir()
        File(srcDir, "main").mkdir()

        val result = sourceSetFolder.sourceFolders

        assertThat(result, containsString("main"))
    }

    @Test
    fun `sourceFolders excludes test directories`() {
        val srcDir = File(tempDir, "src")
        srcDir.mkdir()
        File(srcDir, "main").mkdir()
        File(srcDir, "test").mkdir()
        File(srcDir, "androidTest").mkdir()

        val result = sourceSetFolder.sourceFolders

        assertThat(result, containsString("main"))
        assertThat(result, not(containsString("test")))
        assertThat(result, not(containsString("androidTest")))
    }

    @Test
    fun `sourceFolders joins multiple folders with comma`() {
        val srcDir = File(tempDir, "src")
        srcDir.mkdir()
        File(srcDir, "main").mkdir()
        File(srcDir, "debug").mkdir()

        val result = sourceSetFolder.sourceFolders

        assertThat(result, containsString(","))
    }

    @Test
    fun `testFolders returns empty string when src does not exist`() {
        val result = sourceSetFolder.testFolders

        assertThat(result, equalTo(""))
    }

    @Test
    fun `testFolders returns test folder path`() {
        val srcDir = File(tempDir, "src")
        srcDir.mkdir()
        File(srcDir, "test").mkdir()

        val result = sourceSetFolder.testFolders

        assertThat(result, containsString("test"))
    }

    @Test
    fun `testFolders includes androidTest`() {
        val srcDir = File(tempDir, "src")
        srcDir.mkdir()
        File(srcDir, "test").mkdir()
        File(srcDir, "androidTest").mkdir()

        val result = sourceSetFolder.testFolders

        assertThat(result, containsString("test"))
        assertThat(result, containsString("androidTest"))
    }

    @Test
    fun `testFolders excludes non-test directories`() {
        val srcDir = File(tempDir, "src")
        srcDir.mkdir()
        File(srcDir, "main").mkdir()
        File(srcDir, "test").mkdir()

        val result = sourceSetFolder.testFolders

        assertThat(result, not(containsString("main")))
    }

    @Test
    fun `sourceFiles returns file collection`() {
        val srcDir = File(tempDir, "src")
        srcDir.mkdir()
        File(srcDir, "main").mkdir()

        val result = sourceSetFolder.sourceFiles

        assertThat(result.isEmpty, equalTo(false))
    }

    @Test
    fun `testFiles returns file collection`() {
        val srcDir = File(tempDir, "src")
        srcDir.mkdir()
        File(srcDir, "test").mkdir()

        val result = sourceSetFolder.testFiles

        assertThat(result.isEmpty, equalTo(false))
    }
}
