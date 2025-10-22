package uk.gov.pipelines

import com.android.build.api.dsl.ManagedDevices
import com.android.build.api.dsl.TestOptions
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object TestOptionsMatchers {
    fun hasManagedDevices(matcher: Matcher<ManagedDevices>): Matcher<TestOptions> =
        object : TypeSafeMatcher<TestOptions>() {
            override fun describeTo(p0: Description?) {
                matcher.describeTo(p0)
            }

            override fun describeMismatchSafely(
                item: TestOptions?,
                mismatchDescription: Description?,
            ) {
                matcher.describeMismatch(item?.managedDevices, mismatchDescription)
            }

            override fun matchesSafely(p0: TestOptions?): Boolean = matcher.matches(p0?.managedDevices)
        }
}
