package uk.gov.pipelines.filetree.fetcher

import org.gradle.api.file.FileTree
import org.gradle.api.provider.Provider
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object BaseFileTreeFetcherMatchers {
    fun hasBaseFileTree(matcher: Matcher<Provider<FileTree>>): Matcher<BaseFileTreeFetcher> =
        object : TypeSafeMatcher<BaseFileTreeFetcher>() {
            override fun describeTo(p0: Description?) {
                matcher.describeTo(p0)
            }

            override fun describeMismatchSafely(
                item: BaseFileTreeFetcher?,
                mismatchDescription: Description?,
            ) {
                super.describeMismatchSafely(item, mismatchDescription)
                matcher.describeMismatch(item?.getBaseFileTree(), mismatchDescription)
            }

            override fun matchesSafely(p0: BaseFileTreeFetcher?): Boolean = matcher.matches(p0?.getBaseFileTree())
        }
}
