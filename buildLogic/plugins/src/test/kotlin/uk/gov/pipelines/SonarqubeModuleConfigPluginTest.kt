package uk.gov.pipelines

import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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

        assertEquals(
            EXCLUSIONS,
            properties["sonar.exclusions"],
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

private const val EXCLUSIONS =
    "**/R.class,**/R$*.class,**/BuildConfig.*,**/Manifest*.*,**/*Test*.*,android/**/*.*,**/*FileManager*,**/*AndroidCamera*,**/*AndroidBiometrics*,**/*ContactsProvider*,**/*IntentProvider*,android/databinding/**/*.class,**/android/databinding/*Binding.class,**/android/databinding/*,**/androidx/databinding/*,**/databinding/*,**/BR.*,**/*_MembersInjector.class,**/Dagger*Component.class,**/Dagger*Component\$Builder.class,**/Dagger*Subcomponent*.class,**/*Subcomponent\$Builder.class,**/*Module_*Factory.class,**/dagger/hilt/internal/**/*.*,**/di/module/*,**/*_Factory*.*,**/*Module*.*,**/*Dagger*.*,**/*Hilt*.*,**/*DependenciesProvider*,**/*_GeneratedInjector.*,**/*Args.class,**/*Directions.*,**/*MapperImpl*.*,**/*\$ViewInjector*.*,**/*\$ViewBinder*.*,**/BuildConfig.*,**/*Component*.*,**/*BR*.*,**/Manifest*.*,**/*\$Lambda$*.*,**/*Companion*.*,**/*MembersInjector*.*,**/*_MembersInjector.class,**/*_Factory*.*,**/*_Provide*Factory*.*,**/*Extensions*.*,**/*Extension*.*,**/*\$Result.*,**/*\$Result$*.*,*.json,**/.gradle/**,**/*.gradle*,**/src/test/java/$,**/src/test*/java/$,**/src/androidTest*/java/$,**/src/androidTest/java/$,**/src/*/java/**/developer/**,**/src/*/kotlin/**/developer/**,**/src/*/java/**/developer/**,**/src/*/kotlin/**/developer/**"
