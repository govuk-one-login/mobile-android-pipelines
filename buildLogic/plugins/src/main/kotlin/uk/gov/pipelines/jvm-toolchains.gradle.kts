package uk.gov.pipelines

import com.android.build.api.dsl.CommonExtension

val jvmVersion by extra(21)

configure<JavaPluginExtension> {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jvmVersion))
    }
}

configure<CommonExtension> {
    compileOptions.apply {
        sourceCompatibility = JavaVersion.toVersion(jvmVersion)
        targetCompatibility = JavaVersion.toVersion(jvmVersion)
    }
}
