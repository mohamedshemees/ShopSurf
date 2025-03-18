import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val localProperties = gradleLocalProperties(
    rootDir,
    providers
)
val apiKey = localProperties.getProperty("API_KEY") ?: "\"API_KEY_NOT_FOUND\""
val baseUrl = localProperties.getProperty("BASE_URL") ?: "\"BASE_URL_NOT_FOUND\""
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
    id("kotlinx-serialization")
    id ("androidx.navigation.safeargs.kotlin")



}

android {
    namespace = "dev.mo.surfcart"
    compileSdk = 35




    defaultConfig {

        buildFeatures {
            buildConfig = true // Enable BuildConfig generation
        }

        applicationId = "dev.mo.surfcart"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        buildConfigField("String", "API_KEY", apiKey)
        buildConfigField ("String", "BASE_URL", baseUrl)

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    viewBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)




    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.annotation)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation (libs.retrofit)
    implementation (libs.converter.gson)


    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)


    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // LiveData & Lifecycle

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // RecyclerView
    implementation(libs.androidx.recyclerview.v132)

    // ViewPager2
    implementation(libs.androidx.viewpager2.v100)
    implementation(libs.dotsindicator)


    // Navigation Component (Make sure the version is stable)
    implementation(libs.androidx.navigation.fragment.ktx)  // Use latest stable version
    implementation(libs.androidx.navigation.ui.ktx)

    // Lifecycle (Ensure compatibility with AppCompat)
    implementation(libs.androidx.lifecycle.runtime.ktx.v287)  // Use version 2.7.x
    implementation(libs.androidx.lifecycle.common)



    //supabase
    implementation (libs.postgrest.kt)
    implementation (libs.storage.kt)
    implementation (libs.auth.kt)
    implementation (libs.ktor.client.android)
    implementation (libs.ktor.client.core)
    implementation (libs.ktor.utils)
    implementation(libs.kotlinx.serialization.json) // Use the latest version
}



kapt {
    correctErrorTypes = true
}