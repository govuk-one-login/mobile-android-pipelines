package uk.gov.pipelines

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import uk.gov.pipelines.plugins.BuildConfig

class AndroidSecurityLintConfigPluginTest {
    @Test
    fun `adds android-security-lint dependency to lintChecks configuration`() {
        // GIVEN
        val project = ProjectBuilder.builder().build()
        project.configurations.create("lintChecks")

        // WHEN
        project.pluginManager.apply("uk.gov.pipelines.android-security-lint-config")

        // THEN
        val lintChecksConfiguration = project.configurations.getByName("lintChecks")
        assertEquals(1, lintChecksConfiguration.dependencies.size)

        val declaredDependency = lintChecksConfiguration.dependencies.first()
        with(declaredDependency) {
            assertEquals("lint", name)
            assertEquals("com.android.security.lint", group)
            assertEquals(BuildConfig.ANDROID_SECURITY_LINT_VERSION, version)
        }
    }
}
