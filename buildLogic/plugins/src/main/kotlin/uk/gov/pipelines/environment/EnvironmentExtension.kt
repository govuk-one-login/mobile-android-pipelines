package uk.gov.pipelines.environment

import org.gradle.api.provider.Property

internal interface EnvironmentExtension {
    val environmentVariables: Property<EnvironmentVariables>

    companion object {
        const val NAME = "environment"
    }
}
