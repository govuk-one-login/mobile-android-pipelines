package uk.gov.pipelines.extensions

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import uk.gov.pipelines.extensions.StringExtensions.proseToUpperCamelCase
import uk.gov.pipelines.extensions.StringExtensions.snakeToLowerCamelCase

class StringExtensionsTest {
    @Test
    fun `snakeToLowerCamelCase returns unchanged string when no underscores`() {
        val input = "already"

        val result = input.snakeToLowerCamelCase()

        assertThat(result, equalTo("already"))
    }

    @Test
    fun `snakeToLowerCamelCase converts underscores`() {
        val input = "one_two_three"

        val result = input.snakeToLowerCamelCase()

        assertThat(result, equalTo("oneTwoThree"))
    }

    @Test
    fun `snakeToLowerCamelCase handles uppercase letters after underscore`() {
        val input = "some_Value"

        val result = input.snakeToLowerCamelCase()

        assertThat(result, equalTo("someValue"))
    }

    @Test
    fun `proseToUpperCamelCase converts multiple spaces`() {
        val input = "one two three"

        val result = input.proseToUpperCamelCase()

        assertThat(result, equalTo("OneTwoThree"))
    }

    @Test
    fun `proseToUpperCamelCase returns capitalised string when no spaces`() {
        val input = "already"

        val result = input.proseToUpperCamelCase()

        assertThat(result, equalTo("Already"))
    }

    @Test
    fun `proseToUpperCamelCase handles uppercase letters after space`() {
        val input = "some Value"

        val result = input.proseToUpperCamelCase()

        assertThat(result, equalTo("SomeValue"))
    }
}
