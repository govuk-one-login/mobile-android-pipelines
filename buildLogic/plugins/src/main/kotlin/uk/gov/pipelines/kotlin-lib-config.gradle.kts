package uk.gov.pipelines

listOf(
    "java-library",
    "java-test-fixtures",
    "org.jetbrains.kotlin.jvm",
    "uk.gov.publishing.config",
).forEach {
    project.plugins.apply(it)
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.toVersion(21)
    targetCompatibility = JavaVersion.toVersion(21)
}
