package uk.gov.pipelines.environment

/**
 * This plugin creates the environment extension which gives plugins access to environment variables.
 *
 * Tests may choose to initialise a fake environment in which case this plugin does nothing.
 */
if (project.extensions.findByType<EnvironmentExtension>() == null) {
    val extension =
        project.extensions
            .create<EnvironmentExtension>(EnvironmentExtension.NAME)
    extension.environmentVariables = EnvironmentVariables(System::getenv)
}
