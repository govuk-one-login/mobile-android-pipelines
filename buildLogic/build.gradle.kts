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