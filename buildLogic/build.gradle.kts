import org.sonarqube.gradle.SonarExtension

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin { jvmToolchain(21) }

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.sonarqube)
}

dependencies {
    listOf(
        libs.android.build.tool,
        libs.detekt.gradle,
        libs.kotlin.gradle.plugin,
        libs.ktlint.gradle,
        libs.sonarqube.gradle
    ).forEach { dependency ->
        implementation(dependency)
    }
}

configure<SonarExtension> {
    setAndroidVariant("debug")
    properties {
        property("sonar.projectKey", "mobile-android-pipelines-build-logic")
        property("sonar.projectName", "mobile-android-pipelines-build-logic")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.token", System.getProperty("SONAR_TOKEN"))
        property("sonar.organization", "govuk-one-login")
    }
}
