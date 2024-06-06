buildscript {
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.musicplayer"
    compileSdk = 34

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        applicationId = "com.example.musicplayer"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation ("androidx.wear.compose:compose-navigation:1.2.1")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation ("com.airbnb.android:lottie-compose:6.1.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.wear.compose:compose-foundation:1.2.1")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation ("com.google.android.horologist:horologist-compose-layout:0.5.4")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:")
    implementation ("androidx.compose.runtime:runtime-livedata:")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:")


    // https://mavenlibs.com/maven/dependency/com.google.android.horologist/horologist-media-ui
    implementation ("com.google.android.horologist:horologist-media-ui:0.5.4")
    implementation ("com.google.android.horologist:horologist-media-data:0.5.4")
    implementation ("com.google.android.horologist:horologist-audio-ui:0.5.4")
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.media3:media3-exoplayer-workmanager:1.2.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.6.0")
    // Retrofit with Scalar Converter
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.wear:wear-phone-interactions:1.0.1")

    // Preferences DataStore (SharedPreferences like APIs)
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // Auth
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha03")
    implementation("androidx.credentials:credentials:1.3.0-alpha03")
    implementation("com.google.gms:google-services:4.4.1")
    implementation("com.google.android.gms:play-services-auth:21.1.1")

    implementation("androidx.media3:media3-session:1.3.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.media3:media3-ui:1.3.1")

    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation ("org.mockito:mockito-core:3.11.2")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("androidx.test.espresso:espresso-core:3.3.0")
    testImplementation ("androidx.test:runner:1.5.2")
    testImplementation ("androidx.test:rules:1.5.0")
    testImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation ("androidx.test.uiautomator:uiautomator:2.3.0-alpha03")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation ("io.mockk:mockk:1.13.10")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:3.11.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("androidx.test.uiautomator:uiautomator:2.3.0-alpha03")
    testImplementation ("org.robolectric:robolectric:4.8.1")
    testImplementation ("org.robolectric:shadows-multidex:4.8.1")

    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation( "androidx.test.espresso:espresso-contrib:3.3.0")
    androidTestImplementation( "androidx.test.espresso:espresso-intents:3.3.0")
    androidTestImplementation( "androidx.test.espresso:espresso-accessibility:3.3.0")
    androidTestImplementation( "androidx.test.espresso:espresso-web:3.3.0")
    androidTestImplementation( "androidx.test.espresso.idling:idling-concurrent:3.3.0")
    androidTestImplementation( "androidx.test.espresso:espresso-idling-resource:3.3.0")
    androidTestImplementation ("org.mockito:mockito-android:3.11.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.ext:truth:1.1.1")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0-alpha03")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}