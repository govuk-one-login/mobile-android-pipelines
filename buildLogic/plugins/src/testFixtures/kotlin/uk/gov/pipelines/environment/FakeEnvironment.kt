package uk.gov.pipelines.environment

class FakeEnvironment(
    val variables: MutableMap<String, String>,
) : EnvironmentVariables {
    override fun get(name: String): String? = variables[name]
}
