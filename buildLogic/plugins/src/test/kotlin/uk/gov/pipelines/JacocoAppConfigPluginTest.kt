package uk.gov.pipelines

import com.android.build.api.dsl.CommonExtension
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.pipelines.extras.FAKE_APK_CONFIG
import uk.gov.pipelines.plugins.BuildConfig

class JacocoAppConfigPluginTest {
    private val project = ProjectBuilder.builder().build()

    private lateinit var commonExtension: CommonExtension

    @BeforeEach
    fun setUp() {
        project.rootProject.extraProperties.set("apkConfig", FAKE_APK_CONFIG)
        project.plugins.apply("com.android.application")

        commonExtension = project.extensions.findByType<CommonExtension>()!!
    }

    @Test
    fun `applies jacoco plugin`() {
        project.plugins.apply("uk.gov.pipelines.jacoco-app-config")

        assertThat(project.plugins.findPlugin("jacoco"), notNullValue())
    }

    @Test
    fun `applies jacoco configuration to application extension`() {
        project.plugins.apply("uk.gov.pipelines.jacoco-app-config")

        val jacocoVersion = commonExtension.testCoverage.jacocoVersion

        assertThat(jacocoVersion, equalTo(BuildConfig.JACOCO_TOOL_VERSION))
    }
}
