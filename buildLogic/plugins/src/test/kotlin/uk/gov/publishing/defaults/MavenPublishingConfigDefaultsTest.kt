package uk.gov.publishing.defaults

import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.pipelines.extras.BUILD_LOGIC_DIR
import uk.gov.publishing.MavenPublishingConfigExtension.Companion.mavenPublishingConfig
import uk.gov.publishing.MavenPublishingConfigPlugin

class MavenPublishingConfigDefaultsTest {
    private companion object {
        const val GITHUB_REPOSITORY_NAME = "my-repo"
        const val MAVEN_GROUP_ID = "uk.gov"
    }

    val project = ProjectBuilder.builder().withName("").build()

    @BeforeEach
    fun setUp() {
        project.rootProject.extraProperties.set("githubRepositoryName", GITHUB_REPOSITORY_NAME)
        project.rootProject.extraProperties.set("mavenGroupId", MAVEN_GROUP_ID)
        project.rootProject.extraProperties.set(
            "buildLogicDir",
            BUILD_LOGIC_DIR
        )
        project.plugins.apply("uk.gov.publishing.config")
    }

    @Test
    fun mavenPublishPluginIsApplied() {
        project.plugins.apply("com.android.application")
        project.mavenPublishingConfig()

        val plugin = MavenPublishingConfigPlugin()
        plugin.apply(project)
        val projectPublications = project.extensions.findByType<PublishingExtension>()
        assertNotNull(projectPublications)
    }

    @Test
    fun appModuleDoesNotCreateDefaultPublication() {
        project.plugins.apply("com.android.application")
        project.mavenPublishingConfig()

        val plugin = MavenPublishingConfigPlugin()
        plugin.apply(project)
        val projectPublications = project.extensions.findByType<PublishingExtension>()
        assert(projectPublications!!.publications.isEmpty())
    }

    @Test
    fun libraryModuleCreatesDefaultPublication() {
        project.plugins.apply("com.android.library")
        project.mavenPublishingConfig()

        val plugin = MavenPublishingConfigPlugin()
        plugin.apply(project)
        val projectPublications = project.extensions.findByType<PublishingExtension>()
        assertNotNull(projectPublications?.publications?.findByName("default"))
    }

    @Test
    fun javaModuleCreatesDefaultPublication() {
        project.plugins.apply("java-library")
        project.mavenPublishingConfig()
        project.mavenPublishingConfig().mavenConfigBlock.artifactGroupId.set("")

        val plugin = MavenPublishingConfigPlugin()
        plugin.apply(project)
        val projectPublications = project.extensions.findByType<PublishingExtension>()
        assertNotNull(projectPublications?.publications?.findByName("default"))
    }

//    @Test
//    fun appModuleDoesNotCreateDefaultPublication() {
//        project.plugins.apply("com.android.application")
//        project.mavenPublishingConfig()
//
//        val plugin = MavenPublishingConfigPlugin()
//        plugin.apply(project)
//        val projectPublications = project.extensions.findByType<PublishingExtension>()
//        assert(projectPublications!!.publications.isEmpty())
//    }
}