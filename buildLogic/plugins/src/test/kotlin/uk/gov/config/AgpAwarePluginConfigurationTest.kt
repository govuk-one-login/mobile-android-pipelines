package uk.gov.config

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class AgpAwarePluginConfigurationTest {
    lateinit var testVar: Values
    lateinit var project: Project

    enum class Values {
        DEFAULT,
        APP,
        LIB,
        JAVA_MODULE,
    }

    private val appLibConfig =
        PluginConfiguration<Any> { _, _ ->
            testVar = Values.APP
        }

    private val defaultLibConfig =
        PluginConfiguration<Any> { _, _ ->
            testVar = Values.LIB
        }

    private val javaModuleLibConfig =
        PluginConfiguration<Any> { _, _ ->
            testVar = Values.JAVA_MODULE
        }

    private val sut = AgpAwarePluginConfiguration<Any>(
        appPluginConfiguration = appLibConfig,
        libraryPluginConfiguration = defaultLibConfig,
        javaModulePluginConfiguration = javaModuleLibConfig,
    )

    @BeforeEach
    fun setUp() {
        project = ProjectBuilder.builder().build()
        testVar = Values.DEFAULT
    }

    @Test
    fun detectsAppModule() {
        project.plugins.apply("com.android.application")

        sut.applyConfig(
            project,
            Any()
        )

        assertEquals(testVar, Values.APP)
    }

    @Test
    fun detectsLibraryModule() {
        project.plugins.apply("com.android.library")

        sut.applyConfig(
            project,
            Any()
        )

        assertEquals(testVar, Values.LIB)
    }

    @Test
    fun detectsPureJavaKotlinModule() {
        project.plugins.apply("java-library")

        sut.applyConfig(
            project,
            Any()
        )

        assertEquals(testVar, Values.JAVA_MODULE)
    }

    @Test
    fun detectsNeitherModule() {
        sut.applyConfig(
            project,
            Any()
        )

        assertEquals(testVar, Values.DEFAULT)
    }
}