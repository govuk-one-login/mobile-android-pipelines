import com.android.build.api.dsl.ApplicationExtension
import uk.gov.pipelines.extensions.ProjectExtensions.versionName

plugins {
    id("uk.gov.pipelines.android-app-config")
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "uk.gov.pipelines.testapp"
}

dependencies {
    implementation(project(":test-library"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestUtil(libs.androidx.test.orchestrator)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

configure<ApplicationExtension> {
    val semanticVersion = project.versionName
    val (major, minor, patch) = semanticVersion.split(".")
    val versionCodeFromSemVer =
        major.toInt().times(10000).plus(
            minor.toInt().times(100).plus(
                patch.toInt(),
            ),
        )

    defaultConfig {
        versionName = semanticVersion
        versionCode = 1
    }
}
