package uk.gov.test

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty

/**
 * Extension class that allows consuming modules to configure which test types
 * (e.g. "component", "contract") should have dedicated Gradle tasks created.
 *
 */
open class TestTypeExtension(objects: ObjectFactory) {
    internal val testTypes: ListProperty<String> = objects.listProperty(String::class.java)

    fun testTypes(vararg types: String) {
        testTypes.set(types.toList())
    }
}
