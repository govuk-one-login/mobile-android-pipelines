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
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)
    additionalEditorconfig.set(
        mapOf(
            // The android_studio style is compatible with Android Studio's formatter
            "ktlint_code_style" to "android_studio",
            // Exclude backticked test names from the maximum line length rule
            // See https://ktlint.github.io/ktlint/1.8.0/rules/standard/#max-line-length
            "ktlint_ignore_back_ticked_identifier" to "true",
            "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
        ),
    )
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
