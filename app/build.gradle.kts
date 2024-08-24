plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-kapt")
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.rafdev.practicestv"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rafdev.practicestv"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.hilt.android)
    implementation(libs.androidx.media3.ui.leanback)
    kapt(libs.hilt.android.compiler)
    val nav_version = "2.7.7"

//    implementation("com.google.android.exoplayer:extension-leanback:2.19.1")
    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    implementation("androidx.tvprovider:tvprovider:1.0.0")
    implementation("androidx.leanback:leanback:1.0.0")

    //media3
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")
    implementation ("androidx.media3:media3-exoplayer-hls:1.3.1")
    implementation("androidx.media3:media3-ui-leanback:1.3.1")

}
kapt {
    correctErrorTypes = true
}