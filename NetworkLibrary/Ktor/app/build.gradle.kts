plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("kotlin-kapt")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.ljb.ktor"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ljb.ktor"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //manifestPlaceholders["NAVER_MAP_KEY"] = getLocalProperty("naver_client_id")

        buildConfigField("String", "NAVER_CLIENT_ID", getLocalProperty("naver_client_id"))
        buildConfigField("String", "NAVER_CLIENT_SECRET", getLocalProperty("naver_client_secret"))
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
        dataBinding = true
        buildConfig = true
    }
}

// local.properties 내부에서 key값을 가져오는 함수
fun getLocalProperty(propertyKey: String): String {
    return com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(Lib.Ktor.core)
    implementation(Lib.Ktor.cio)
    implementation(Lib.Ktor.logging)
    implementation(Lib.Ktor.contentNegotiation)
    implementation(Lib.Ktor.serializationJson)
}