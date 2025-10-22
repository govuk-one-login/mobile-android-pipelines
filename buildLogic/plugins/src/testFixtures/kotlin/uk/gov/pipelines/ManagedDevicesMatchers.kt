package uk.gov.pipelines

import com.android.build.api.dsl.ManagedDevices
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object ManagedDevicesMatchers {
    fun hasDevice(deviceName: String): Matcher<ManagedDevices> =
        object : TypeSafeMatcher<ManagedDevices>() {
            override fun describeTo(p0: Description?) {
                p0?.appendText("has device name: $deviceName")
            }

            override fun describeMismatchSafely(
                item: ManagedDevices?,
                mismatchDescription: Description?,
            ) {
                mismatchDescription?.appendText("$deviceName not found")
            }

            override fun matchesSafely(p0: ManagedDevices?): Boolean = p0?.devices?.findByName(deviceName) != null
        }
}
