package uk.gov.pipelines

import com.android.build.gradle.LibraryExtension
import uk.gov.pipelines.extensions.LibraryExtensionExt.decorateExtensionWithJacoco
import uk.gov.pipelines.extensions.ProjectExtensions.debugLog
import uk.gov.pipelines.extensions.generateDebugJacocoTasks
import com.android.build.api.dsl.LibraryExtension as DslLibraryExtension

project.plugins.apply("uk.gov.pipelines.jacoco-common-config")

project.configure<DslLibraryExtension> {
    decorateExtensionWithJacoco().also {
        project.debugLog("Applied jacoco properties to Library")
    }
}

project.configure<LibraryExtension> {
    decorateExtensionWithJacoco().also {
        project.debugLog("Applied jacoco properties to Library")
    }
}

project.afterEvaluate {
    (this.findProperty("android") as? LibraryExtension)?.let { extension ->
        extension.libraryVariants.all {
            generateDebugJacocoTasks(project)
        }
    }
}
