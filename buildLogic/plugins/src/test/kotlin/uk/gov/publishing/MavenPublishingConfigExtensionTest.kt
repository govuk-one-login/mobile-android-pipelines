package uk.gov.publishing

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import uk.gov.pipelines.publishing.MavenPublishingConfigProjectExtensions.GITHUB_REPOSITORY_NAME
import uk.gov.pipelines.publishing.MavenPublishingConfigProjectExtensions.MAVEN_GROUP_ID
import uk.gov.pipelines.publishing.MavenPublishingConfigProjectExtensions.configurePublishingPlugin
import uk.gov.publishing.MavenPublishingConfigExtension.Companion.mavenPublishingConfig

class MavenPublishingConfigExtensionTest {
    private val project = ProjectBuilder.builder().build()

    @Test
    fun `it reads root project extra configuration`() {
        project.configurePublishingPlugin(
            githubRepositoryName = GITHUB_REPOSITORY_NAME,
            mavenGroupId = MAVEN_GROUP_ID,
        )
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
