package uk.gov.publishing.defaults

import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Spy
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import uk.gov.pipelines.config.buildLogicDir
import uk.gov.pipelines.extensions.ProjectExtensions.execWithOutput
//import uk.gov.pipelines.extensions.ProjectExtensions.execWithOutput
import uk.gov.pipelines.extensions.ProjectExtensions.versionName
import uk.gov.publishing.MavenPublishingConfigExtension.Companion.mavenPublishingConfig
import uk.gov.publishing.MavenPublishingConfigPlugin
import java.io.File
import java.io.OutputStream

class MavenPublishingConfigDefaultsTest {
    private companion object {
        const val GITHUB_REPOSITORY_NAME = "my-repo"
        const val MAVEN_GROUP_ID = "uk.gov"
    }

    val project = spy(ProjectBuilder.builder().build())

    @BeforeEach
    fun setUp() {
        project.extraProperties.set(
            "buildLogicDir",
            "mobile-android-pipelines/buildLogic"
        )
        project.rootProject.extraProperties.set(
            "buildLogicDir",
            "mobile-android-pipelines/buildLogic"
        )
        project.rootProject.extraProperties.set("githubRepositoryName", GITHUB_REPOSITORY_NAME)
        project.rootProject.extraProperties.set("mavenGroupId", MAVEN_GROUP_ID)
        project.rootProject.extraProperties.set(
            "buildLogicDir",
            "mobile-android-pipelines/buildLogic"
        )
        project.plugins.apply("uk.gov.publishing.config")
        val scriptsDir = project.buildLogicDir.resolve("scripts/")
        doReturn("0.1.0").`when`<String>(project.execWithOutput {
            workingDir = project.rootDir
            executable = "bash"
            standardOutput = OutputStream.nullOutputStream()
            args =
                listOf(
                    scriptsDir.resolve("getCurrentVersion").path,
                )
        })
    }

    @Test
    fun appModuleDoesNotCreateDefaultPublication() {
        project.plugins.apply("com.android.application")
        project.mavenPublishingConfig()

        val plugin = MavenPublishingConfigPlugin()
        plugin.apply(project)
        assertNull(project.tasks.findByName("publishDefaultPublicationToLocalBuildRepository"))
    }

    @Test
    fun libraryModuleCreatesDefaultPublication() {
        project.plugins.apply("com.android.library")
        project.mavenPublishingConfig()

        val plugin = MavenPublishingConfigPlugin()
        plugin.apply(project)
        assertNotNull(project.tasks.findByName("publishDefaultPublicationToLocalBuildRepository"))
        assertNull(project.tasks.findByName("publishJavaPublicationToLocalBuildRepository"))
    }

    @Test
    fun javaModuleCreatesDefaultPublication() {
        project.plugins.apply("java-library")
        project.mavenPublishingConfig()

        val plugin = MavenPublishingConfigPlugin()
        plugin.apply(project)
        assertNotNull(project.tasks.findByName("publishDefaultPublicationToLocalBuildRepository"))
    }
}