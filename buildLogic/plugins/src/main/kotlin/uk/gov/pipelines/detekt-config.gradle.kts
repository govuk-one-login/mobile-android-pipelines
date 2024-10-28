package uk.gov.pipelines

import io.gitlab.arturbosch.detekt.extensions.DetektExtension

project.plugins.apply("io.gitlab.arturbosch.detekt")

configure<DetektExtension>(setupDetekt())

fun setupDetekt(): DetektExtension.() -> Unit = {
    val configOverrideFile = file(
        "${project.rootProject.projectDir}/config/detekt/detektConfig.yml"
    )
    val detektToolVersion = "1.23.1"

    allRules = true
    buildUponDefaultConfig = true
    if (configOverrideFile.exists()) {
        logger.info("Detekt configuration: Using config file: $configOverrideFile")
        config.from(configOverrideFile)
    }
    debug = false
    disableDefaultRuleSets = false
    ignoreFailures = false
    parallel = true
    source.from(
        fileTree("src") {
            include(
                listOf("**/java/**/*.*")
            )
        }
    )
    toolVersion = detektToolVersion
}
