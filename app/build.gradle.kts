plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.strk.jarvislauncher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.strk.jarvislauncher"
        minSdk = 26          // Android 8.0 — needed for foreground service notification channel APIs
        targetSdk = 34       // Android 14, matches Samsung F15 5G's One UI 6.1
        versionCode = 1
        versionName = "0.1.0-skeleton"
    }

    buildTypes {
        release {
            isMinifyEnabled = false // keep off until modules are stable; ProGuard can break reflection-based libs (Room, Vosk JNI)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // --- Core ---
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2") // app drawer grid
    implementation("androidx.lifecycle:lifecycle-service:2.8.3") // for the wake-word foreground service

    // --- Room (local-only storage, never synced — see data/ module) ---
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // --- Offline speech-to-text: Vosk (small en-us model, loaded on-demand only) ---
    implementation("com.alphacephei:vosk-android:0.3.47")
    implementation("net.java.dev.jna:jna:5.13.0@aar") // Vosk's native bridge dependency

    // --- Offline wake word: Porcupine ("Hi Strk" custom .ppn model, see assets/models/README) ---
    implementation("ai.picovoice:porcupine-android:3.0.2")

    // --- Coroutines, for keeping STT/NLU work off the main thread ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
}
