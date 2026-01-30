package uk.gov.pipelines

import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.sonarqube.gradle.ActionBroadcast
import org.sonarqube.gradle.SonarExtension
import org.sonarqube.gradle.SonarProperties

class SonarqubeModuleConfigPluginTest {
    private val project = ProjectBuilder.builder().build()

    @Test
    fun `it applies sonar plugin`() {
        project.pluginManager.apply(SonarqubeModuleConfigPlugin::class.java)

        assertNotNull(project.extensions.findByType<SonarExtension>())
    }

    @Test
    fun `it configures sonar extension`() {
        val properties = mutableMapOf<String, Any>()

        project.applySonarModuleConfigPlugin(outputProperties = properties)

        assertTrue(
            properties["sonar.exclusions"].toString()
                .contains("uitestwrapper/src/*/java/**,uitestwrapper/src/*/kotlin/**"),
        )
    }

    private fun Project.applySonarModuleConfigPlugin(outputProperties: MutableMap<String, Any>) {
        val sonarProperties = SonarProperties(outputProperties)
        val propertiesActions = ActionBroadcast<SonarProperties>()
        extensions.create(
            "testSonar",
            SonarExtension::class.java,
            propertiesActions,
        )
        pluginManager.apply(SonarqubeModuleConfigPlugin::class.java)

        propertiesActions.execute(sonarProperties)
    }
}
