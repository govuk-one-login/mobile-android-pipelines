package uk.gov.pipelines

import uk.gov.pipelines.extensions.ProjectExtensions.versionName

listOf(
    "uk.gov.pipelines.vale-config",
    "uk.gov.pipelines.sonarqube-root-config",
).forEach {
    project.plugins.apply(it)
}

version = versionName
