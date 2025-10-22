package uk.gov.pipelines.filetree.fetcher

import org.gradle.testfixtures.ProjectBuilder
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.FieldSource
import uk.gov.pipelines.ProviderMatchers.isPresent
import uk.gov.pipelines.filetree.fetcher.BaseFileTreeFetcherMatchers.hasBaseFileTree

class BaseFileTreeFetcherTest {
    @ParameterizedTest
    @FieldSource("fetchers")
    fun `Obtains a file tree for use in task generation`(fetcher: BaseFileTreeFetcher) {
        project.pluginManager.apply("com.android.library")

        assertThat(
            "There should be a generated FileTree!",
            fetcher,
            hasBaseFileTree(isPresent()),
        )
    }

    companion object {
        private val project = ProjectBuilder.builder().build()

        @JvmStatic
        private val fetchers =
            listOf(
                AsmFileTreeFetcher(
                    project = project,
                    variant = "debug",
                    capitalisedVariantFlavorName = "Debug",
                ),
                JavaCompileFileTreeFetcher(
                    project = project,
                    variant = "debug",
                    capitalisedVariantFlavorName = "Debug",
                ),
                KotlinCompileFileTreeFetcher(
                    project = project,
                    variant = "debug",
                    capitalisedVariantFlavorName = "Debug",
                ),
            )
    }
}
