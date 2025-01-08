package uk.gov.pipelines.environment

internal interface EnvironmentVariables {
    fun get(name: String): String?
}
