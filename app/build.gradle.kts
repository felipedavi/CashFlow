import com.android.sdklib.AndroidVersion.VersionCodes.VANILLA_ICE_CREAM
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.ksp)
    id("androidx.navigation.safeargs")
}

android {
    namespace = "meimaonamassa.cashflow"
    compileSdk = 36

    defaultConfig {
        applicationId = "meimaonamassa.cashflow"
        minSdk = 21
        targetSdk = VANILLA_ICE_CREAM
        versionCode = 4
        versionName = "1.1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    buildFeatures {
        viewBinding = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
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
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    // Lifecycle components
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Backport Android ThreeTen
    implementation(libs.threetenabp)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
