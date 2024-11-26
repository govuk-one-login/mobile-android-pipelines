package uk.gov.pipelines.extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.AppExtension
import com.android.build.gradle.LibraryExtension
import uk.gov.pipelines.plugins.BuildConfig
import com.android.build.api.dsl.LibraryExtension as DslLibraryExtension

/**
 * Wrapper object for containing extension functions relating to various implementations of the
 * [LibraryExtension], such as the [DslLibraryExtension] variant.
 */
object LibraryExtensionExt {
    /**
     * Apply Jacoco configurations to the `android` block of a gradle module.
     *
     * Sets up the `android.testCoverage.jacocoVersion` property with the [version] parameter.
     * Also enables instrumentation and unit test coverage.
     */
    fun LibraryExtension.decorateExtensionWithJacoco() {
        testCoverage.jacocoVersion = BuildConfig.JACOCO_TOOL_VERSION
        buildTypes {
            debug {
                this.enableAndroidTestCoverage = true
                this.enableUnitTestCoverage = true
            }
        }
    }

    /**
     * Apply Jacoco configurations to the `android` block of a gradle module.
     *
     * Sets up the `android.testCoverage.jacocoVersion` property with the [version] parameter.
     * Also enables instrumentation and unit test coverage.
     */
    fun DslLibraryExtension.decorateExtensionWithJacoco() {
        testCoverage.jacocoVersion = BuildConfig.JACOCO_TOOL_VERSION
        buildTypes {
            debug {
                this.enableAndroidTestCoverage = true
                this.enableUnitTestCoverage = true
            }
        }
    }

    fun ApplicationExtension.decorateExtensionWithJacoco() {
        testCoverage.jacocoVersion = BuildConfig.JACOCO_TOOL_VERSION
        buildTypes {
            debug {
                this.enableAndroidTestCoverage = true
                this.enableUnitTestCoverage = true
            }
        }
    }

    fun AppExtension.decorateExtensionWithJacoco() {
        this.jacoco.jacocoVersion = BuildConfig.JACOCO_TOOL_VERSION
        buildTypes {
            this.maybeCreate("debug").apply {
                this.enableAndroidTestCoverage = true
                this.enableUnitTestCoverage = true
            }
        }
    }
}
