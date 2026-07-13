package uk.gov.pipelines.extensions

import com.android.build.api.dsl.LibraryExtension
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItems
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.pipelines.extensions.BaseExtensions.baseAndroidConfig
import uk.gov.pipelines.extras.FAKE_APK_CONFIG

class BaseAndroidConfigTest {
    private val project = ProjectBuilder.builder().build()

    private lateinit var libraryExtension: LibraryExtension

    @BeforeEach
    fun setUp() {
        project.rootProject.extraProperties.set("apkConfig", FAKE_APK_CONFIG)
        project.plugins.apply("com.android.library")

        libraryExtension = project.extensions.findByType<LibraryExtension>()!!
    }

    @Test
    fun `sets compileSdk from apkConfig`() {
        libraryExtension.baseAndroidConfig(project)

        assertThat(libraryExtension.compileSdk, equalTo(FAKE_APK_CONFIG.sdkVersions.compile))
    }

    @Test
    fun `sets minSdk from apkConfig`() {
        libraryExtension.baseAndroidConfig(project)

        val minSdk = libraryExtension.defaultConfig.minSdk

        assertThat(minSdk, equalTo(FAKE_APK_CONFIG.sdkVersions.minimum))
    }

    @Test
    fun `sets targetSdk from apkConfig`() {
        libraryExtension.baseAndroidConfig(project)

        val targetSdk = libraryExtension.testOptions.targetSdk

        assertThat(targetSdk, equalTo(FAKE_APK_CONFIG.sdkVersions.target))
    }

    @Test
    fun `enables includeAndroidResources for unit tests`() {
        libraryExtension.baseAndroidConfig(project)

        val includeAndroidResources =
            libraryExtension.testOptions.unitTests.isIncludeAndroidResources

        assertThat(includeAndroidResources, equalTo(true))
    }

    @Test
    fun `excludes files from packaging`() {
        libraryExtension.baseAndroidConfig(project)

        val excludes = libraryExtension.packaging.resources.excludes

        assertThat(excludes, hasItems("META-INF/LICENSE*"))
    }
}
