package uk.gov.pipelines.extensions

import com.android.build.api.dsl.CommonExtension
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.pipelines.extensions.LibraryExtensionExt.decorateExtensionWithJacoco
import uk.gov.pipelines.plugins.BuildConfig

class DecorateExtensionWithJacocoTest {
    private val project = ProjectBuilder.builder().build()

    private lateinit var commonExtension: CommonExtension

    @BeforeEach
    fun setUp() {
        project.plugins.apply("com.android.library")

        commonExtension = project.extensions.findByType<CommonExtension>()!!
    }

    @Test
    fun `sets jacocoVersion from BuildConfig`() {
        commonExtension.decorateExtensionWithJacoco()

        val jacocoVersion = commonExtension.testCoverage.jacocoVersion

        assertThat(jacocoVersion, equalTo(BuildConfig.JACOCO_TOOL_VERSION))
    }

    @Test
    fun `enables android test coverage on debug build type`() {
        commonExtension.decorateExtensionWithJacoco()

        val enableAndroidTestCoverage =
            commonExtension.buildTypes
                .named("debug")
                .get()
                .enableAndroidTestCoverage

        assertThat(enableAndroidTestCoverage, equalTo(true))
    }

    @Test
    fun `enables unit test coverage on debug build type`() {
        commonExtension.decorateExtensionWithJacoco()

        val enableUnitTestCoverage =
            commonExtension.buildTypes
                .named("debug")
                .get()
                .enableUnitTestCoverage

        assertThat(enableUnitTestCoverage, equalTo(true))
    }
}
