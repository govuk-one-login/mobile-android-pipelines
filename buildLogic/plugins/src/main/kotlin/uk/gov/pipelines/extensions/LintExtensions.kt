package uk.gov.pipelines.extensions

import com.android.build.api.dsl.Lint
import java.io.File

object LintExtensions {
    fun configureLintOptions(configDir: String): Lint.() -> Unit =
        {
            abortOnError = true
            absolutePaths = true
            baseline = File("$configDir/android/baseline.xml")
            checkAllWarnings = true
            checkDependencies = false
            checkGeneratedSources = false
            checkReleaseBuilds = true
            disable.addAll(
                setOf(
                    "ConvertToWebp",
                    "UnusedIds",
                    "VectorPath",
                ),
            )
            informational.addAll(
                setOf(
                    // Manage dependency updates using Dependabot
                    // https://gds-way.digital.cabinet-office.gov.uk/standards/tracking-dependencies.html
                    "NewerVersionAvailable",
                    "AndroidGradlePluginVersion",
                ),
            )
            explainIssues = true
            htmlReport = true
            ignoreTestSources = true
            ignoreWarnings = false
            lintConfig = File("$configDir/android/lint.xml")
            noLines = false
            quiet = false
            showAll = true
            textReport = true
            warningsAsErrors = true
            xmlReport = true
        }
}
