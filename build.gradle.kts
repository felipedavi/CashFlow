plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    id("androidx.navigation.safeargs") version Versions.NAVIGATION_VERSION apply false
}