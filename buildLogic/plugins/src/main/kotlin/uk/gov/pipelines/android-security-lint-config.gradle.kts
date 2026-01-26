package uk.gov.pipelines

import uk.gov.pipelines.plugins.BuildConfig.ANDROID_SECURITY_LINT_VERSION

dependencies {
    add("lintChecks", "com.android.security.lint:lint:${ANDROID_SECURITY_LINT_VERSION}")
}
