package uk.gov.pipelines

listOf(
    "uk.gov.pipelines.android-app-base-config",
    "uk.gov.signing.app-signing-config",
    "uk.gov.pipelines.detekt-config",
    "uk.gov.pipelines.emulator-config",
    "uk.gov.pipelines.jacoco-app-config",
    "uk.gov.pipelines.ktlint-config",
    "uk.gov.pipelines.sonarqube-module-config",
).forEach {
    project.plugins.apply(it)
}
