package uk.gov.pipelines.testlibrary

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Text that says 'hello, world'.
 */
@Composable
fun HelloWorld(modifier: Modifier = Modifier) {
    BasicText(
        modifier = modifier,
        text = "Hello, world",
    )
}
