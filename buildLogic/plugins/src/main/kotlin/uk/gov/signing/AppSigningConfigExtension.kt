package uk.gov.signing

import org.gradle.api.provider.Property

/**
 * Extension used to configure the [AppSigningConfigPlugin].
 */
interface AppSigningConfigExtension {
    val keystoreFilePath: Property<String>
    val keystorePassword: Property<String>
    val keyAlias: Property<String>
    val keyPassword: Property<String>

    companion object {
        const val NAME = "appSigning"
    }
}
