package uk.gov.pipelines

import com.android.build.api.dsl.ManagedDevices
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.equalTo
import org.hamcrest.TypeSafeMatcher

object ManagedDevicesMatchers {
    fun hasSize(expected: Int): Matcher<ManagedDevices> = hasSize(equalTo(expected))

    fun hasSize(matcher: Matcher<Int>): Matcher<ManagedDevices> =
        object : TypeSafeMatcher<ManagedDevices>() {
            override fun describeTo(p0: Description?) {
                matcher.describeTo(p0)
            }

            override fun describeMismatchSafely(
                item: ManagedDevices?,
                mismatchDescription: Description?,
            ) {
                matcher.describeMismatch(item?.devices?.size, mismatchDescription)
            }

            override fun matchesSafely(p0: ManagedDevices?): Boolean = matcher.matches(p0?.devices?.size)
        }
}
