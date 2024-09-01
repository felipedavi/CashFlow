plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "meimaonamassa.cashflow"
    compileSdk = 34

    defaultConfig {
        applicationId = "meimaonamassa.cashflow"
        minSdk = 21
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.0-a.3"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    // Room components
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    // Lifecycle components
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // Backport Android ThreeTen
    implementation(libs.threetenabp)
}