package uk.gov.pipelines

import com.android.build.api.dsl.TestOptions
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.equalTo
import org.hamcrest.TypeSafeMatcher

object TestOptionsMatchers {
    fun hasAnimationsDisabled(matcher: Matcher<Boolean>): Matcher<TestOptions> =
        object : TypeSafeMatcher<TestOptions>() {
            override fun describeTo(p0: Description?) {
                matcher.describeTo(p0)
            }

            override fun describeMismatchSafely(
                item: TestOptions?,
                mismatchDescription: Description?,
            ) {
                matcher.describeMismatch(item?.animationsDisabled, mismatchDescription)
            }

            override fun matchesSafely(p0: TestOptions?): Boolean = matcher.matches(p0?.animationsDisabled)
        }

    fun hasAnimationsDisabled(): Matcher<TestOptions> = hasAnimationsDisabled(equalTo(true))

    fun hasAnimationsEnabled(): Matcher<TestOptions> = hasAnimationsDisabled(equalTo(false))

    fun hasExecution(expected: String) = hasExecution(equalTo(expected))

    fun hasExecution(matcher: Matcher<String>): Matcher<TestOptions> =
        object : TypeSafeMatcher<TestOptions>() {
            override fun describeTo(p0: Description?) {
                matcher.describeTo(p0)
            }

            override fun describeMismatchSafely(
                item: TestOptions?,
                mismatchDescription: Description?,
            ) {
                matcher.describeMismatch(item?.execution, mismatchDescription)
            }

            override fun matchesSafely(p0: TestOptions?): Boolean = matcher.matches(p0?.execution)
        }
}
