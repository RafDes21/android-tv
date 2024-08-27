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
        multiDexEnabled = true
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
    implementation(libs.androidx.leanback)
    implementation(libs.hilt.android)
//    implementation(libs.androidx.leanback.extension)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.leanback)
    implementation(libs.glide)
    implementation (libs.glide.transformations)
//    import jp.wasabeef.glide.transformations.BorderTransformation
    kapt(libs.hilt.android.compiler)
    val nav_version = "2.7.7"
    implementation("androidx.multidex:multidex:2.0.1")

//    implementation 'com.google.android.exoplayer:exoplayer:2.x.x' // Reemplaza 2.x.x por la última versión
//    implementation 'com.google.android.exoplayer:extension-ima:2.x.x'
    implementation("androidx.media3:media3-exoplayer-ima:1.1.1")

//    implementation("com.google.android.exoplayer:extension-leanback:2.19.1")
    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    implementation("androidx.tvprovider:tvprovider:1.0.0")

}
kapt {
    correctErrorTypes = true
}