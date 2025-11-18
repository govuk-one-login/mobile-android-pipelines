package uk.gov.pipelines

listOf(
    "uk.gov.pipelines.vale-config",
    "uk.gov.pipelines.sonarqube-root-config",
).forEach {
    project.plugins.apply(it)
}
