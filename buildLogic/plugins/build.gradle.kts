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

    // Transitive dependencies of the plugin classpath (AGP, Kotlin Gradle plugin, sonarqube)
    // flagged by Dependabot. These exist only on the build/plugin classpath, never in any
    // shipped app, but are constrained here to patched versions to clear the alerts.
    constraints {
        implementation(libs.patch.guava) {
            because("Earlier versions contain known vulnerabilities (GHSA-xxph-c9ww-hj94)")
        }
        implementation(libs.patch.logback.core) {
            because("Earlier versions contain known vulnerabilities (GHSA-jhq6-gfmj-v8fx)")
        }
        // The bouncycastle modules must move together — mixing bcprov with older
        // bcutil/bcpkix causes a NoSuchFieldError at runtime.
        implementation(libs.bundles.tooling.security.bouncycastle) {
            because("Earlier versions contain known vulnerabilities (GHSA-9pwp-9qqc-pr26, GHSA-qp49-qgx5-5m26)")
        }
    }

    listOf(
        libs.android.build.tool,
        libs.org.hamcrest,
    ).forEach(::testFixturesApi)

    listOf(
        libs.kotlin.gradle.plugin,
    ).forEach(::testFixturesImplementation)

    listOf(
        platform(libs.org.junit.bom),
        libs.org.junit.jupiter
            .asProvider(),
        libs.org.junit.jupiter.params,
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
    version =
        libs.versions.ktlint.cli
            .get()

    filter {
        exclude {
            it.file.absolutePath
                .contains("/build/")
        }
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("skipped", "failed")
    }
}

project.configure<JacocoPluginExtension> {
    toolVersion =
        libs.versions.jacoco.tool
            .get()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports.xml.required = true
}

buildConfig {
    buildConfigField(
        "ANDROID_SECURITY_LINT_VERSION",
        libs.versions.android.security.lint
            .get(),
    )
    buildConfigField("DETEKT_TOOL_VERSION", libs.versions.detekt.get())
    buildConfigField(
        "JACOCO_TOOL_VERSION",
        libs.versions.jacoco.tool
            .get(),
    )
    buildConfigField(
        "KTLINT_CLI_VERSION",
        libs.versions.ktlint.cli
            .get(),
    )
}
