package uk.gov.pipelines

import com.android.build.api.dsl.LibraryExtension
import uk.gov.pipelines.extensions.BaseExtensions.baseAndroidConfig
import uk.gov.pipelines.extensions.LintExtensions.configureLintOptions
import uk.gov.pipelines.extensions.ProjectExtensions.versionName

listOf(
    "com.android.library",
    "uk.gov.pipelines.detekt-config",
    "uk.gov.pipelines.emulator-config",
    "uk.gov.pipelines.jacoco-lib-config",
    "uk.gov.pipelines.jvm-toolchains",
    "uk.gov.pipelines.ktlint-config",
    "uk.gov.pipelines.sonarqube-module-config",
    "uk.gov.publishing.config",
    "uk.gov.pipelines.android-security-lint-config",
).forEach {
    project.plugins.apply(it)
}

version = versionName

configure<LibraryExtension> {
    baseAndroidConfig(project)
    lint(configureLintOptions("${rootProject.projectDir}/config"))
}
