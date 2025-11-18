package uk.gov.pipelines

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.BaseExtension
import uk.gov.pipelines.extensions.BaseExtensions.baseAndroidConfig
import uk.gov.pipelines.extensions.LintExtensions.configureLintOptions

listOf(
    "com.android.application",
    "org.jetbrains.kotlin.android",
    "uk.gov.pipelines.jvm-toolchains",
).forEach {
    project.plugins.apply(it)
}

configure<BaseExtension> {
    baseAndroidConfig(project)
}

configure<ApplicationExtension> {
    lint(configureLintOptions("${rootProject.projectDir}/config"))
}
