plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

    // Add the dependency for the Kotlin Gradle plugin
    id("com.google.devtools.ksp")

    // Add the dependency for the Hilt Android Gradle plugin
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.peter.iwanttobelieve"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.peter.iwanttobelieve"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.googleid)
    implementation(libs.material3)
    implementation(libs.androidx.navigationevent)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:34.15.0"))

    // When using the BoM, you don't specify versions in Firebase library dependencies

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-analytics")

    // TODO: Add the dependencies for any other Firebase products you want to use
    // See https://firebase.google.com/docs/android/setup#available-libraries
    // For example, add the dependencies for Firebase Authentication and Cloud Firestore

    // Declare the dependency for the Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")

    // Declare the dependency for the Firebase Firestore library
    implementation("com.google.firebase:firebase-firestore")

    // Declare the dependency for the Firebase Storage library
    implementation("com.google.firebase:firebase-storage")

    // Import the Google Fonts library
    implementation("androidx.compose.ui:ui-text-google-fonts:1.9.0")

    // Add the dependency for the Hilt Android Gradle plugin
    implementation("com.google.dagger:hilt-android:2.57.1")

    // Add the dependency for the Hilt Android Gradle plugin
    ksp("com.google.dagger:hilt-android-compiler:2.57.1")

    // Add the dependency for the Hilt Android Gradle plugin
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Add the dependency for the Coil library
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Add the dependency for the Google services Gradle plugin
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
}