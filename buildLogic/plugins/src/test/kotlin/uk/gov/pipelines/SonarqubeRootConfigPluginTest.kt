package uk.gov.pipelines

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

class SonarqubeRootConfigPluginTest {
    private val project = ProjectBuilder.builder().build()

    private companion object {
        const val SONAR_TOKEN = "123"
    }

    @BeforeEach
    fun setup() {
        project.rootProject.extraProperties.set(
            "buildLogicDir",
            // Relative to where test projects are created
            "../../../../../../",
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
    fun `it configures sonar plugin`() {
        val sonarProperties = SonarProperties(mutableMapOf())
        val propertiesActions = ActionBroadcast<SonarProperties>()
        project.extensions.create(
            "testSonar",
            SonarExtension::class.java,
            propertiesActions,
        )

        project.pluginManager.apply(SonarqubeRootConfigPlugin::class.java)
        propertiesActions.execute(sonarProperties)

        assertEquals(
            "https://sonarcloud.io",
            sonarProperties.properties["sonar.host.url"],
        )
        assertEquals(
            SONAR_TOKEN,
            sonarProperties.properties["sonar.token"],
        )
        assertEquals(
            "govuk-one-login",
            sonarProperties.properties["sonar.organization"],
        )
        assertNotNull(
            sonarProperties.properties["sonar.projectVersion"],
        )
    }
}
