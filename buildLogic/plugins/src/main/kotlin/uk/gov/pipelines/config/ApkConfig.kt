package uk.gov.pipelines.config

interface ApkConfig {
    val applicationId: String
    val debugVersion: String
    val sdkVersions: SdkVersions

    interface SdkVersions {
        val minimum: Int
        val target: Int
        val compile: Int
    }
}
