plugins {
    id("com.android.application") version "8.2.2"
    id("org.jetbrains.kotlin.android") version "1.9.0"
    id("com.google.gms.google-services") version "4.4.2"
    id("com.chaquo.python")
}

android {
    namespace = "com.example.formula1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.formula1"
        minSdk = 32
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Compatible with Kotlin 1.9.0
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    /* ─────────── PYTHON FLAVOR (3.8) ─────────── */
    flavorDimensions += "pyVersion"
    productFlavors {
        create("py38") {
            dimension = "pyVersion"
        }
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

/* ─────────── CHAQUOPY CONFIG ─────────── */
chaquopy {
    productFlavors {
        getByName("py38") { version = "3.8" }
    }

    defaultConfig {
        buildPython("C:\\Users\\Daniel\\AppData\\Local\\Programs\\Python\\Python38-32\\python.exe")

        pip {
            install("scikit-learn")
            install("pandas")
            install("openpyxl")
            install("joblib")
        }
    }
}

dependencies {

    // Core AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Jetpack Compose BOM (Bill of Materials)
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))

    // Compose UI & Material
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.compose.foundation:foundation")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.2.3"))
    implementation("com.google.firebase:firebase-auth")

    // Google Play Services Auth
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // SceneView
    implementation("io.github.sceneview:sceneview:2.3.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("com.opencsv:opencsv:5.7.1")
    implementation("androidx.wear.compose:compose-material:1.4.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

