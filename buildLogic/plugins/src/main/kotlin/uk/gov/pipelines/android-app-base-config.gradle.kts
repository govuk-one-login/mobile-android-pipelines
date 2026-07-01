package uk.gov.pipelines

import com.android.build.api.dsl.ApplicationExtension
import uk.gov.pipelines.extensions.BaseExtensions.baseAndroidConfig
import uk.gov.pipelines.extensions.LintExtensions.configureLintOptions
import uk.gov.pipelines.extensions.ProjectExtensions.versionName

listOf(
    "com.android.application",
    "uk.gov.pipelines.jvm-toolchains",
).forEach {
    project.plugins.apply(it)
}

version = versionName

configure<ApplicationExtension> {
    baseAndroidConfig(project)
    lint(configureLintOptions("${rootProject.projectDir}/config"))
}
