package uk.gov.pipelines

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import uk.gov.pipelines.config.GradleProperties
import uk.gov.pipelines.config.GradleProperties.rootProjectGradleProperty
import uk.gov.pipelines.detekt.PipelinesDetektPluginExtension

project.plugins.apply("io.gitlab.arturbosch.detekt")

project.extensions.create<PipelinesDetektPluginExtension>("pipelinesDetekt")

project.afterEvaluate {
    configure<DetektExtension>(setupDetekt())
}

fun setupDetekt(): DetektExtension.() -> Unit = {
    val detektToolVersion = "1.23.7"
    val customConfigFile = the<PipelinesDetektPluginExtension>().configFile.orNull?.let(::file)

    val pipelinesDir = rootProjectGradleProperty(GradleProperties.DIR)
    val defaultConfigFile =
        file("${rootProject.projectDir}/$pipelinesDir/config/detekt-default.yml")
    logger.info("Detekt configuration: Using default config: ${defaultConfigFile.absolutePath}")
    if (customConfigFile != null) {
        logger.info("Detekt configuration: Also using custom config: $customConfigFile")
    }

    allRules = true
    buildUponDefaultConfig = true
    config.from(defaultConfigFile)
    if(customConfigFile != null) {
        config.from(customConfigFile)
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
