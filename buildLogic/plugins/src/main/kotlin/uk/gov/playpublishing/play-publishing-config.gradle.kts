package uk.gov.playpublishing

import com.android.build.api.dsl.ApplicationExtension
import com.github.triplet.gradle.play.PlayPublisherExtension
import com.github.triplet.gradle.androidpublisher.ResolutionStrategy

val googlePlayServiceAccountJson = project.rootProject.file("config/service-account-credentials.json")
val signingKeystoreFile = project.rootProject.file("config/keystore.jks")
val signingKeystorePassword: String? = System.getenv("KEYSTORE_PASSWORD")
val signingKeyAlias: String? = System.getenv("KEYSTORE_KEY_ALIAS")
val signingKeyPassword: String? = System.getenv("KEYSTORE_KEY_PASSWORD")

if (!googlePlayServiceAccountJson.exists()) {
    project.logger.warn("Google Service Account credentials not found.")
}
project.logger.info("Google Service Account credentials found: $googlePlayServiceAccountJson")

if (!signingKeystoreFile.exists()) {
    project.logger.warn("Signing keystore not found")
}
project.logger.info("Signing keystore found: $signingKeystoreFile")

project.plugins.apply("com.github.triplet.play")

configure<ApplicationExtension> {
    signingConfigs {
        register("release") {
            storeFile = signingKeystoreFile
            storePassword = signingKeystorePassword
            keyAlias = signingKeyAlias
            keyPassword = signingKeyPassword
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

configure<PlayPublisherExtension> {
    serviceAccountCredentials.set(googlePlayServiceAccountJson)
    track.set("internal")
    userFraction.set(1.0)
    resolutionStrategy.set(ResolutionStrategy.AUTO)
}