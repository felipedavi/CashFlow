plugins {
    alias(libs.plugins.android.application) apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN_VERSION apply false
    id("androidx.navigation.safeargs") version Versions.NAVIGATION_VERSION apply false
}