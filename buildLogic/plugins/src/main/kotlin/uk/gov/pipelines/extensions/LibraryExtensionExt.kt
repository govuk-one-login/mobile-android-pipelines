package uk.gov.pipelines.extensions

import com.android.build.api.dsl.CommonExtension
import uk.gov.pipelines.plugins.BuildConfig

/**
 * Wrapper object for containing extension functions relating to the android extension's
 * jacoco configuration.
 */
object LibraryExtensionExt {
    /**
     * Apply Jacoco configurations to the `android` block of a gradle module.
     *
     * Sets up the `android.testCoverage.jacocoVersion` property with the jacoco tool version.
     * Also enables instrumentation and unit test coverage.
     */
    fun CommonExtension.decorateExtensionWithJacoco() {
        testCoverage.apply {
            jacocoVersion = BuildConfig.JACOCO_TOOL_VERSION
        }
        buildTypes.apply {
            named("debug") {
                enableAndroidTestCoverage = true
                enableUnitTestCoverage = true
            }
        }
    }
}
