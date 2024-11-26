package uk.gov.pipelines

import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import uk.gov.pipelines.plugins.BuildConfig

project.plugins.apply(
    "org.jlleitschuh.gradle.ktlint",
)

configure<KtlintExtension> {
    version.set(BuildConfig.KTLINT_CLI_VERSION)
    debug.set(true)
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.JSON)
        reporter(ReporterType.CHECKSTYLE)
    }
    filter {
        exclude("**/generated/**")
        include(listOf("${project.projectDir}/src/**/*.kt"))
    }
}
