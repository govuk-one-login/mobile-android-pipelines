package uk.gov.signing

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.SigningConfig
import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.pipelines.environment.EnvironmentExtension
import uk.gov.pipelines.environment.FakeEnvironment
import uk.gov.pipelines.extras.BUILD_LOGIC_DIR
import uk.gov.pipelines.extras.FAKE_APK_CONFIG
import java.nio.file.Paths

class AppSigningConfigPluginTest {
    private val project =
        ProjectBuilder
            .builder()
            .build()

    private val environment = FakeEnvironment(mutableMapOf())

    companion object {
        private const val ENV_KEYSTORE_PASSWORD = "envkeystorepassword"
        private const val ENV_KEY_ALIAS = "envkey"
        private const val ENV_KEY_PASSWORD = "envkeypassword"
        private const val KEYSTORE_PASSWORD = "keystorepassword"
        private const val KEY_ALIAS = "key"
        private const val KEY_PASSWORD = "keypassword"
    }

    @BeforeEach
    fun setup() {
        project.rootProject.extraProperties.set("buildLogicDir", BUILD_LOGIC_DIR)
        project.rootProject.extraProperties.set("apkConfig", FAKE_APK_CONFIG)
        project.createFakeEnvironmentExtension()
    }

    @Test
    fun `given no extension, it creates the signing config extension`() {
        project.givenPluginIsApplied()

        assertNotNull(project.extensions.findByType<AppSigningConfigExtension>())
    }

    @Test
    fun `given extension already exists, it configures the existing extension`() {
        val preexistingExtension =
            project.extensions.create<AppSigningConfigExtension>("preexistingExtension")
        project.givenPluginIsApplied()

        assertEquals(
            preexistingExtension,
            project.extensions.findByType<AppSigningConfigExtension>(),
        )
    }

    @Test
    fun `given no configuration or environment variables, it doesn't apply any signing config`() {
        project.givenPluginIsApplied()
        project.givenPluginExtensionConfiguration(
            keystoreFilePath = null,
            keystorePassword = null,
            keyAlias = null,
            keyPassword = null,
        )

        project.assertNoSigningConfig()
    }

    @Test
    fun `given invalid keystore path, it doesn't apply any signing config`() {
        project.givenPluginIsApplied()
        project.givenPluginExtensionConfiguration(
            keystoreFilePath = "non-existent-dir/keystore.jks",
        )

        project.assertNoSigningConfig()
    }

    @Test
    fun `given missing keystore password, it doesn't apply any signing config`() {
        project.givenPluginIsApplied()
        project.givenPluginExtensionConfiguration(
            keystorePassword = null,
        )

        project.assertNoSigningConfig()
    }

    @Test
    fun `given missing key alias, it doesn't apply any signing config`() {
        project.givenPluginIsApplied()
        project.givenPluginExtensionConfiguration(
            keyAlias = null,
        )

        project.assertNoSigningConfig()
    }

    @Test
    fun `given missing key password, it doesn't apply any signing config`() {
        project.givenPluginIsApplied()
        project.givenPluginExtensionConfiguration(
            keyPassword = null,
        )
        project.pluginManager.apply(AppSigningConfigPlugin::class.java)

        project.assertNoSigningConfig()
    }

    @Test
    fun `given valid config, it applies the signing config`() {
        project.givenPluginIsApplied()
        project.givenPluginExtensionConfiguration(
            keystoreFilePath = testKeystorePath(),
            keystorePassword = KEYSTORE_PASSWORD,
            keyAlias = KEY_ALIAS,
            keyPassword = KEY_PASSWORD,
        )

        val signingConfig = project.evaluateReleaseSigningConfig()

        assertEquals(testKeystorePath(), signingConfig?.storeFile?.absolutePath)
        assertEquals(KEYSTORE_PASSWORD, signingConfig?.storePassword)
        assertEquals(KEY_ALIAS, signingConfig?.keyAlias)
        assertEquals(KEY_PASSWORD, signingConfig?.keyPassword)
    }

    @Test
    fun `given valid environment, it applies the signing config`() {
        givenValidEnvironment()
        project.givenPluginIsApplied()

        val signingConfig = project.evaluateReleaseSigningConfig()

        assertEquals(testKeystorePath(), signingConfig?.storeFile?.absolutePath)
        assertEquals(ENV_KEYSTORE_PASSWORD, signingConfig?.storePassword)
        assertEquals(ENV_KEY_ALIAS, signingConfig?.keyAlias)
        assertEquals(ENV_KEY_PASSWORD, signingConfig?.keyPassword)
    }

    @Test
    fun `given valid environment, user signing config takes precedence`() {
        givenValidEnvironment()
        project.givenPluginIsApplied()
        project.givenPluginExtensionConfiguration(
            keystoreFilePath = testKeystorePath(),
            keystorePassword = KEYSTORE_PASSWORD,
            keyAlias = KEY_ALIAS,
            keyPassword = KEY_PASSWORD,
        )

        val signingConfig = project.evaluateReleaseSigningConfig()

        assertEquals(testKeystorePath(), signingConfig?.storeFile?.absolutePath)
        assertEquals(KEYSTORE_PASSWORD, signingConfig?.storePassword)
        assertEquals(KEY_ALIAS, signingConfig?.keyAlias)
        assertEquals(KEY_PASSWORD, signingConfig?.keyPassword)
    }

    private fun Project.assertNoSigningConfig() = assertNull(evaluateReleaseSigningConfig())

    private fun Project.evaluateReleaseSigningConfig(): SigningConfig? {
        (this as ProjectInternal).evaluate()
        val appExtension = extensions.findByType<ApplicationExtension>()!!
        return appExtension.buildTypes.findByName("release")!!.signingConfig
    }

    private fun testKeystorePath() = Paths.get("src/test/resources", "test-keystore.jks").toAbsolutePath().toString()

    private fun Project.givenPluginIsApplied() {
        project.pluginManager.apply(AppSigningConfigPlugin::class.java)
        project.configure<ApplicationExtension> { namespace = "uk.gov.pipelines" }
    }

    private fun givenValidEnvironment() =
        environment.apply {
            variables[AppSigningEnvironmentKeys.SIGNING_STORE_FILE_PATH] = testKeystorePath()
            variables[AppSigningEnvironmentKeys.SIGNING_STORE_PASSWORD] = ENV_KEYSTORE_PASSWORD
            variables[AppSigningEnvironmentKeys.SIGNING_KEY_ALIAS] = ENV_KEY_ALIAS
            variables[AppSigningEnvironmentKeys.SIGNING_KEY_PASSWORD] = ENV_KEY_PASSWORD
        }

    private fun Project.givenPluginExtensionConfiguration(
        keystoreFilePath: String? = testKeystorePath(),
        keystorePassword: String? = KEYSTORE_PASSWORD,
        keyAlias: String? = KEY_ALIAS,
        keyPassword: String? = KEY_PASSWORD,
    ) = project.configure<AppSigningConfigExtension> {
        keystoreFilePath?.let(this.keystoreFilePath::set)
        keystorePassword?.let(this.keystorePassword::set)
        keyAlias?.let(this.keyAlias::set)
        keyPassword?.let(this.keyPassword::set)
    }

    private fun Project.createFakeEnvironmentExtension() {
        val environmentExtension =
            project.extensions.create<EnvironmentExtension>("testEnvironment")
        environmentExtension.environmentVariables.set(environment)
    }
}
