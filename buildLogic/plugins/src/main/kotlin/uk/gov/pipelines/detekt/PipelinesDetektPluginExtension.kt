package uk.gov.pipelines.detekt

import org.gradle.api.provider.Property

/**
 * Extension for configuring the detekt convention plugin
 */
interface PipelinesDetektPluginExtension {
    val configFile: Property<String>
}
