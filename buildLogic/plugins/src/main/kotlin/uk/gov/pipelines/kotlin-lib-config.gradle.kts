package uk.gov.pipelines

listOf(
    "java-library",
    "org.jetbrains.kotlin.jvm",
    "uk.gov.publishing.config",
).forEach {
    project.plugins.apply(it)
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.toVersion(21)
    targetCompatibility = JavaVersion.toVersion(21)
}
