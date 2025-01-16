package uk.gov.pipelines.extras

import uk.gov.pipelines.config.AndroidSdkVersions

/**
 * Relative to where test projects are created
 */
const val BUILD_LOGIC_DIR = "../../../../../../"

val FakeAndroidSdkVersions =
    AndroidSdkVersions(
        minSdk = 29,
        targetSdk = 34,
        compileSdk = 35,
    )
