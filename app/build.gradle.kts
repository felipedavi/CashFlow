import com.android.sdklib.AndroidVersion.VersionCodes.M
import com.android.sdklib.AndroidVersion.VersionCodes.VANILLA_ICE_CREAM

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
    id("androidx.navigation.safeargs.kotlin")
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "meimaonamassa.cashflow"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

        defaultConfig {
            applicationId = "meimaonamassa.cashflow"
            minSdk = M
            targetSdk = VANILLA_ICE_CREAM
            versionCode = 7
            versionName = "2.5.0"
        }

        buildTypes {
            release {
                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        buildFeatures {
            viewBinding = true
        }
    }

    dependencies {
        // AndroidX Core Components
        implementation(libs.androidx.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.androidx.constraintlayout)

        // Material Design
        implementation(libs.material)

        // Navigation Component
        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.navigation.ui.ktx)

        // Coroutines
        implementation(libs.kotlinx.coroutines.core)

        // Room components
        implementation(libs.androidx.room.ktx)
        implementation(libs.review)
        implementation(libs.review.ktx)
        ksp(libs.androidx.room.compiler)

        // Lifecycle components
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.lifecycle.livedata.ktx)

        // Backport Android ThreeTen
        implementation(libs.threetenabp)

        // MPAndroidchart
        implementation(libs.mpandroidchart)
    }

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}