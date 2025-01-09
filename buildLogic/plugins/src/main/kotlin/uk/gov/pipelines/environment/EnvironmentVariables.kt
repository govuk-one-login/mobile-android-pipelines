package uk.gov.pipelines.environment

internal fun interface EnvironmentVariables {
    fun get(name: String): String?
}
