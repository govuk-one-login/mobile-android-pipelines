package uk.gov.pipelines

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.BaseExtension
import uk.gov.pipelines.extensions.BaseExtensions.baseAndroidConfig
import uk.gov.pipelines.extensions.LintExtensions.configureLintOptions
import uk.gov.pipelines.extensions.ProjectExtensions.versionName

listOf(
    "com.android.application",
    "org.jetbrains.kotlin.android",
    "uk.gov.pipelines.jvm-toolchains",
).forEach {
    project.plugins.apply(it)
}

version = versionName

configure<BaseExtension> {
    baseAndroidConfig(project)
}

configure<ApplicationExtension> {
    lint(configureLintOptions("${rootProject.projectDir}/config"))
}
