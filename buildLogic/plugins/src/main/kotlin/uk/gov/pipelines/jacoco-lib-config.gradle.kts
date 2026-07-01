package uk.gov.pipelines

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.kotlin.dsl.configure
import uk.gov.pipelines.extensions.LibraryExtensionExt.decorateExtensionWithJacoco
import uk.gov.pipelines.extensions.ProjectExtensions.debugLog
import uk.gov.pipelines.extensions.generateDebugJacocoTasks

project.plugins.apply("uk.gov.pipelines.jacoco-common-config")

project.configure<CommonExtension> {
    decorateExtensionWithJacoco().also {
        project.debugLog("Applied jacoco properties to Library")
    }
}

project.extensions.configure<LibraryAndroidComponentsExtension> {
    onVariants(selector().withBuildType("debug")) { variant ->
        variant.generateDebugJacocoTasks(project)
    }
}
