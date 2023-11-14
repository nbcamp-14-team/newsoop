plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "com.nbcamp_14_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nbcamp_14_project"
        minSdk = 31
        targetSdk = 34
        versionCode = 2
        versionName = "1.1"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true //뷰바인딩
        dataBinding = true
    }
}

dependencies {
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //retrofit and converter
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //okhttp
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    //coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.0")
    implementation("com.google.guava:guava:27.0.1-android")
    //cardView
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0-rc03")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    //coil
    implementation("io.coil-kt:coil:1.1.0")
    //serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    //scalars
    implementation("com.squareup.retrofit2:converter-scalars:2.6.4")
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.6.4")
    //Jsoup
    implementation("org.jsoup:jsoup:1.13.1")
    //indicator
    implementation("com.tbuonomo:dotsindicator:5.0")
    implementation("org.jsoup:jsoup:1.16.1")
    //naver
    implementation("com.navercorp.nid:oauth-jdk8:5.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    //viewmodel
    implementation("androidx.fragment:fragment-ktx:1.4.1")
    //firestore
    implementation("com.google.firebase:firebase-firestore-ktx")

    implementation("com.google.firebase:firebase-firestore-ktx:24.3.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.0.2")
    implementation("com.github.bumptech.glide:glide:4.13.2")
    implementation("com.firebaseui:firebase-ui-storage:8.0.1")
    implementation("com.github.bumptech.glide:compiler:4.13.2")
    //swipe
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //오픈소스 라이선스
    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
}