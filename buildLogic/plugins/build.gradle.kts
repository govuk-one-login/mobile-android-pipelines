plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `java-test-fixtures`
    jacoco
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.buildconfig)
}

group = "uk.gov.pipelines"

gradlePlugin {
    plugins {
        register("uk.gov.publishing.config") {
            this.id = this.name
            implementationClass = "uk.gov.publishing.MavenPublishingConfigPlugin"
        }
        register("uk.gov.signing.app-signing-config") {
            this.id = this.name
            implementationClass = "uk.gov.signing.AppSigningConfigPlugin"
        }
    }
}

dependencies {
    listOf(
        libs.android.build.tool,
        libs.detekt.gradle,
        libs.kotlin.gradle.plugin,
        libs.ktlint.gradle,
        libs.sonarqube.gradle,
    ).forEach { dependency ->
        implementation(dependency)
    }

    listOf(
        platform(libs.org.junit.bom),
        libs.org.junit.jupiter,
        libs.org.hamcrest,
    ).forEach { dep: Provider<MinimalExternalModuleDependency> ->
        testImplementation(dep)
    }

    listOf(
        libs.org.junit.platform.launcher,
    ).forEach { dep ->
        testRuntimeOnly(dep)
    }
}

ktlint {
    filter {
        exclude { it.file.absolutePath.contains("/build/") }
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("skipped", "failed")
    }
}

project.configure<JacocoPluginExtension> {
    toolVersion = libs.versions.jacoco.tool.get()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports.xml.required = true
}

buildConfig {
    buildConfigField("DETEKT_TOOL_VERSION", libs.versions.detekt.get())
    buildConfigField("JACOCO_TOOL_VERSION", libs.versions.jacoco.tool.get())
    buildConfigField("KTLINT_CLI_VERSION", libs.versions.ktlint.cli.get())
}
