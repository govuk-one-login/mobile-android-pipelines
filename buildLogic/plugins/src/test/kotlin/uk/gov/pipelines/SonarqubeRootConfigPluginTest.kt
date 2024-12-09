package uk.gov.pipelines

import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.sonarqube.gradle.ActionBroadcast
import org.sonarqube.gradle.SonarExtension
import org.sonarqube.gradle.SonarProperties
import uk.gov.pipelines.extras.BUILD_LOGIC_DIR

class SonarqubeRootConfigPluginTest {
    private val project = ProjectBuilder.builder().build()

    private companion object {
        const val SONAR_TOKEN = "123"
    }

    @BeforeEach
    fun setup() {
        project.rootProject.extraProperties.set(
            "buildLogicDir",
            BUILD_LOGIC_DIR,
        )
        project.rootProject.extraProperties.set(
            "sonarProperties",
            mapOf("" to ""),
        )
        System.setProperty("SONAR_TOKEN", SONAR_TOKEN)
    }

    @Test
    fun `it applies sonar plugin`() {
        project.pluginManager.apply(SonarqubeRootConfigPlugin::class.java)

        assertNotNull(project.extensions.findByType<SonarExtension>())
    }

    @Test
    fun `it configures sonar extension`() {
        val properties = mutableMapOf<String, Any>()

        project.applySonarRootConfigPlugin(outputProperties = properties)

        assertEquals(
            "https://sonarcloud.io",
            properties["sonar.host.url"],
        )
        assertEquals(
            SONAR_TOKEN,
            properties["sonar.token"],
        )
        assertEquals(
            "govuk-one-login",
            properties["sonar.organization"],
        )
        assertNotNull(
            properties["sonar.projectVersion"],
        )
    }

    @Test
    fun `it allows default properties to be overridden`() {
        val properties = mutableMapOf<String, Any>()
        project.rootProject.extraProperties.set(
            "sonarProperties",
            mapOf(
                "sonar.host.url" to "localhost",
            ),
        )

        project.applySonarRootConfigPlugin(outputProperties = properties)

        assertEquals(
            "localhost",
            properties["sonar.host.url"],
        )
    }

    @Test
    fun `it allows properties to be added`() {
        val properties = mutableMapOf<String, Any>()
        project.rootProject.extraProperties.set(
            "sonarProperties",
            mapOf(
                "sonar.custom" to "my custom property",
            ),
        )

        project.applySonarRootConfigPlugin(outputProperties = properties)

        assertEquals(
            "my custom property",
            properties["sonar.custom"],
        )
    }

    private fun Project.applySonarRootConfigPlugin(outputProperties: MutableMap<String, Any>) {
        val sonarProperties = SonarProperties(outputProperties)
        val propertiesActions = ActionBroadcast<SonarProperties>()
        extensions.create(
            "testSonar",
            SonarExtension::class.java,
            propertiesActions,
        )
        pluginManager.apply(SonarqubeRootConfigPlugin::class.java)

        propertiesActions.execute(sonarProperties)
    }
}
