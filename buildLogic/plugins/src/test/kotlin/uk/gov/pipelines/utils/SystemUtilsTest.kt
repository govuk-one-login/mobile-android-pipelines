package uk.gov.pipelines.utils

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SystemUtilsTest {
    @Test
    fun `isWindows returns true when system property contains windows`() {
        // This test would need to mock System.getProperty to be reliable
        // For now, we can only test the current system
        val result = isWindows()
        val osName = System.getProperty("os.name").lowercase()

        if (osName.contains("windows")) {
            assertTrue(result)
        } else {
            assertFalse(result)
        }
    }
}
