package uk.gov.pipelines.extras

import uk.gov.pipelines.config.ApkConfig

class FakeApkConfig : ApkConfig {
    override val applicationId: String = "uk.gov.pipelines.test"
    override val debugVersion: String = "DEBUG_VERSION"
    override val sdkVersions =
        object : ApkConfig.SdkVersions {
            override val minimum = 29
            override val target = 35
            override val compile = 35
        }
}
