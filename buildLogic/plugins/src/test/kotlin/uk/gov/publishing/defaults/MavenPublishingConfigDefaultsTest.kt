package uk.gov.publishing.defaults

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPom
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import uk.gov.pipelines.extensions.ProjectExtensions.versionName
import uk.gov.pipelines.extras.BUILD_LOGIC_DIR
import uk.gov.publishing.MavenPublishingConfigExtension.Companion.mavenPublishingConfig
import uk.gov.publishing.MavenPublishingConfigPlugin
import uk.gov.publishing.handlers.MavenPublishingConfigHandler

class MavenPublishingConfigDefaultsTest {
    private companion object {
        const val GITHUB_REPOSITORY_NAME = "my-repo"
        const val MAVEN_GROUP_ID = "uk.gov"
        const val PROJECT_NAME = "test-project-name"

        // The artifact ID should match the project name by default
        const val MAVEN_ARTIFACT_ID = PROJECT_NAME
    }

    private val plugin = MavenPublishingConfigPlugin()
    private val project = ProjectBuilder.builder().withName(PROJECT_NAME).build()

    @BeforeEach
    fun setUp() {
        project.rootProject.extraProperties.set("githubRepositoryName", GITHUB_REPOSITORY_NAME)
        project.rootProject.extraProperties.set("mavenGroupId", MAVEN_GROUP_ID)
        project.rootProject.extraProperties.set(
            "buildLogicDir",
            BUILD_LOGIC_DIR,
        )
        project.plugins.apply("uk.gov.publishing.config")
    }

    @Test
    fun `given app module, it does not register publications`() {
        project.plugins.apply("com.android.application")

        plugin.apply(project)

        assertTrue(project.expectMavenPublishingExtension().publications.isEmpty())
    }

    @Test
    fun `given library module, it registers default publication`() {
        project.plugins.apply("com.android.library")

        plugin.apply(project)

        project
            .expectMavenPublishingExtension()
            .expectDefaultPublication()
            .assertExpectedDefaults()
    }

    @Test
    fun `given java module, it registers default publication`() {
        project.plugins.apply("java-library")

        plugin.apply(project)

        project
            .expectMavenPublishingExtension()
            .expectDefaultPublication()
            .assertExpectedDefaults()
    }

    private fun MavenPublication.assertExpectedDefaults() {
        assertEquals(MAVEN_ARTIFACT_ID, this.artifactId)
        assertEquals(MAVEN_GROUP_ID, this.groupId)
        assertEquals(project.versionName, this.version)
        assertPomMatches(project.mavenConfigDefaults, this.pom)
    }

    private fun assertPomMatches(
        expected: MavenPublishingConfigHandler,
        actual: MavenPom,
    ) {
        assertInstanceOf<DefaultMavenPom>(actual)
        assertEquals(1, actual.licenses.size)
        val license = actual.licenses[0]
        assertEquals(expected.license.name.get(), license.name.get())
        assertEquals(expected.license.url.get(), license.url.get())
        assertEquals(1, actual.developers.size)
        val developer = actual.developers[0]
        assertEquals(expected.developer.id.get(), developer.id.get())
        assertEquals(expected.developer.name.get(), developer.name.get())
        assertEquals(expected.developer.email.get(), developer.email.get())
        assertEquals(expected.scm.url.get(), actual.scm.url.get())
        assertEquals(expected.scm.connection.get(), actual.scm.connection.get())
        assertEquals(expected.scm.developerConnection.get(), actual.scm.developerConnection.get())
    }

    private val Project.mavenConfigDefaults: MavenPublishingConfigHandler
        get() =
            project.mavenPublishingConfig().mavenConfigBlock

    private fun Project.expectMavenPublishingExtension(): PublishingExtension {
        val extension = extensions.findByType<PublishingExtension>()
        assertNotNull(extension)
        return extension!!
    }

    private fun PublishingExtension.expectDefaultPublication(): MavenPublication {
        val defaultPublication = publications.findByName("default")
        assertNotNull(defaultPublication)
        assertInstanceOf<MavenPublication>(defaultPublication)
        return defaultPublication
    }
}
