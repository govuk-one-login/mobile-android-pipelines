package uk.gov.pipelines

listOf(
    "uk.gov.pipelines.vale-config",
).forEach {
    project.plugins.apply(it)
}
