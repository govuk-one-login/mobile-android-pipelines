plugins {
    id("uk.gov.pipelines.android-lib-config")
}

android {
    namespace = "uk.gov.pipelines.testlibrary"
}

dependencies {
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestUtil(libs.androidx.test.orchestrator)
}
