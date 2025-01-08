package uk.gov.pipelines.environment

internal object SystemEnvironmentVariables : EnvironmentVariables {
    override fun get(name: String): String? = System.getenv(name)
}
