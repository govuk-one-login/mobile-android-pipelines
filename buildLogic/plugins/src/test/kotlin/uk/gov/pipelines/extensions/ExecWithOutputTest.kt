package uk.gov.pipelines.extensions

import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import uk.gov.pipelines.extensions.ProjectExtensions.execWithOutput

class ExecWithOutputTest {
    private val project = ProjectBuilder.builder().build()

    @Test
    fun `execWithOutput returns trimmed command output`() {
        val result =
            project.execWithOutput {
                commandLine("echo", "hello")
            }

        assertThat(result, equalTo("hello"))
    }

    @Test
    fun `execWithOutput trims leading and trailing whitespace`() {
        val result =
            project.execWithOutput {
                commandLine("printf", "  spaced  ")
            }

        assertThat(result, equalTo("spaced"))
    }

    @Test
    fun `execWithOutput returns multiline output trimmed`() {
        val result =
            project.execWithOutput {
                commandLine("printf", "line1\nline2\n")
            }

        assertThat(result, equalTo("line1\nline2"))
    }
}
