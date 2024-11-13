package uk.gov.pipelines.emulator

/**
 * Configuration for the emulator configuration plugin.
 *
 * @param[systemImageSources] Set of system images to generate managed devices for
 * @param[androidApiLevels] Set of Android API levels generate managed devices for
 * @param[deviceFilters] Set of filters for device profiles. For example "Pixel XL".
 *   A device profile will be included if it contains the string provided (case insensitive).
 */
data class EmulatorConfig(
    val systemImageSources: Set<SystemImageSource>,
    val androidApiLevels: Set<Int>,
    val deviceFilters: Set<String>,
)
