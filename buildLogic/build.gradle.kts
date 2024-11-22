import org.sonarqube.gradle.SonarExtension

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin { jvmToolchain(17) }

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
        property("sonar.projectKey", "mobile-android-pipelines")
        property("sonar.projectName", "mobile-android-pipelines")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.token", System.getProperty("SONAR_TOKEN"))
        property("sonar.organization", "govuk-one-login")
    }
}
