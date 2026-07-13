package uk.gov.pipelines

import com.android.build.api.dsl.CommonExtension
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.pipelines.plugins.BuildConfig

class JacocoLibConfigPluginTest {
    private val project = ProjectBuilder.builder().build()

    private lateinit var commonExtension: CommonExtension

    @BeforeEach
    fun setUp() {
        project.plugins.apply("com.android.library")

        commonExtension = project.extensions.findByType<CommonExtension>()!!
    }

    @Test
    fun `applies jacoco plugin`() {
        project.plugins.apply("uk.gov.pipelines.jacoco-lib-config")

        assertThat(project.plugins.findPlugin("jacoco"), notNullValue())
    }

    @Test
    fun `applies jacoco configuration to library extension`() {
        project.plugins.apply("uk.gov.pipelines.jacoco-lib-config")

        val jacocoVersion = commonExtension.testCoverage.jacocoVersion

        assertThat(jacocoVersion, equalTo(BuildConfig.JACOCO_TOOL_VERSION))
    }
}
