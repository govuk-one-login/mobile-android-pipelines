package uk.gov.pipelines

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import uk.gov.pipelines.config.buildLogicDir

project.plugins.apply("io.gitlab.arturbosch.detekt")

configure<DetektExtension>(setupDetekt())

fun setupDetekt(): DetektExtension.() -> Unit = {
    val configDefaultFile = file(
        "${project.buildLogicDir}/config/detekt/detektConfig.yml"
    )
    val configOverrideFile = file(
        "${project.rootProject.projectDir}/config/detekt/detektConfig.yml"
    )
    val detektToolVersion = "1.23.7"

    allRules = true
    buildUponDefaultConfig = true
    config.from(configDefaultFile)
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
