package uk.gov.publishing

import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import uk.gov.publishing.MavenPublishingConfigExtension.Companion.mavenPublishingConfig

class MavenPublishingConfigExtensionTest {
    private companion object {
        const val GITHUB_REPOSITORY_NAME = "my-repo"
        const val MAVEN_GROUP_ID = "uk.gov"
    }

    private val project = ProjectBuilder.builder().build()

    @Test
    fun `it reads root project extra configuration`() {
        project.rootProject.extraProperties.set("githubRepositoryName", GITHUB_REPOSITORY_NAME)
        project.rootProject.extraProperties.set("mavenGroupId", MAVEN_GROUP_ID)
        val expectedUrl = "https://github.com/govuk-one-login/$GITHUB_REPOSITORY_NAME"
        val expectedUrlInsecure = "http://github.com/govuk-one-login/$GITHUB_REPOSITORY_NAME"
        val expectedConnectionGit = "scm:git:git://github.com/govuk-one-login/$GITHUB_REPOSITORY_NAME.git"
        val expectedConnectionSsh = "scm:git:ssh://github.com/govuk-one-login/$GITHUB_REPOSITORY_NAME.git"

        val result = project.mavenPublishingConfig().mavenConfigBlock

        assertEquals(MAVEN_GROUP_ID, result.artifactGroupId.get())
        assertEquals(expectedUrl, result.url.get())
        assertEquals(expectedUrlInsecure, result.scm.url.get())
        assertEquals(expectedConnectionGit, result.scm.connection.get())
        assertEquals(expectedConnectionSsh, result.scm.developerConnection.get())
    }
}
