package uk.gov.pipelines

import org.gradle.api.provider.Provider
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object ProviderMatchers {
    fun <T : Any?> isPresent(): Matcher<Provider<T>> =
        object : TypeSafeMatcher<Provider<T>>() {
            override fun describeTo(p0: Description?) {
                p0?.appendText("has present file matcher!")
            }

            override fun describeMismatchSafely(
                item: Provider<T>?,
                mismatchDescription: Description?,
            ) {
                mismatchDescription?.appendText("Provided without a value")
            }

            override fun matchesSafely(p0: Provider<T>?): Boolean = p0?.isPresent ?: false
        }
}
