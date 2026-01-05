package uk.gov.signing

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType
import uk.gov.pipelines.environment.EnvironmentExtension
import uk.gov.pipelines.extensions.BaseExtensions.baseAndroidConfig

/**
 * Configures app signing for release builds.
 *
 * Creates an Android app signing configuration and applies it to release builds.
 *
 * The plugin reads the default signing configuration from the environment
 * (see [AppSigningEnvironmentKeys]).
 *
 * To override the signing configuration, use the [AppSigningConfigExtension].
 *
 */
class AppSigningConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("uk.gov.pipelines.environment.environment-config")
        target.plugins.apply("uk.gov.pipelines.android-app-base-config")

        val extension =
            target.extensions.findByType<AppSigningConfigExtension>()
                ?: target.extensions.create<AppSigningConfigExtension>(AppSigningConfigExtension.NAME)

        val environment =
            target.extensions
                .findByType<EnvironmentExtension>()!!
                .environmentVariables
                .get()

        with(extension) {
            keystorePassword.convention(environment.get(AppSigningEnvironmentKeys.SIGNING_STORE_PASSWORD))
            keystoreFilePath.convention(environment.get(AppSigningEnvironmentKeys.SIGNING_STORE_FILE_PATH))
            keyAlias.convention(environment.get(AppSigningEnvironmentKeys.SIGNING_KEY_ALIAS))
            keyPassword.convention(environment.get(AppSigningEnvironmentKeys.SIGNING_KEY_PASSWORD))
        }

        target.extensions
            .getByType<ApplicationAndroidComponentsExtension>()
            .finalizeDsl { androidComponentsExtension: ApplicationExtension ->
                androidComponentsExtension.createSigningConfig(target, extension)
            }
    }

    @Suppress("ReturnCount")
    private fun ApplicationExtension.createSigningConfig(
        target: Project,
        extension: AppSigningConfigExtension,
    ) {
        if (!extension.keystoreFilePath.isPresent) {
            target.logger.warn("Signing plugin has been applied but keystore file path not provided")
            return
        }

        val signingKeystoreFile = target.rootProject.file(extension.keystoreFilePath)

        if (!signingKeystoreFile.exists()) {
            target.logger.error("Signing keystore not found: $signingKeystoreFile")
            return
        }

        if (!extension.keystorePassword.isPresent) {
            target.logger.error("Please set keystorePassword")
            return
        }

        if (!extension.keyPassword.isPresent) {
            target.logger.error("Please set keyPassword")
            return
        }

        if (!extension.keyAlias.isPresent) {
            target.logger.error("Please set keyAlias")
            return
        }

        target.project.logger.info("Signing keystore found: $signingKeystoreFile")
        target.project.configure<BaseExtension> {
            baseAndroidConfig(target)
        }

        signingConfigs {
            register("release") {
                storeFile = signingKeystoreFile
                storePassword = extension.keystorePassword.get()
                keyAlias = extension.keyAlias.get()
                keyPassword = extension.keyPassword.get()
            }
        }
        buildTypes {
            release {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
}
