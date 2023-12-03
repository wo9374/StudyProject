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

    //Ktor
    implementation("io.ktor:ktor-client-android:${rootProject.extra["ktor_version"]}")
    implementation("io.ktor:ktor-client-cio:${rootProject.extra["ktor_version"]}")                  //JVM, Android 및 Native 플랫폼에서 사용할 수 있는 완전 비동기식 코루틴 기반 엔진
    implementation("io.ktor:ktor-client-logging-jvm:${rootProject.extra["ktor_version"]}")          //HTTP Request을 로깅하기 위해 사용
    implementation("io.ktor:ktor-client-content-negotiation:${rootProject.extra["ktor_version"]}")  //직렬화/역직렬화를 위한 ContentNegotiation
    implementation("io.ktor:ktor-serialization-kotlinx-json:${rootProject.extra["ktor_version"]}")  //Serialization json
    //implementation("io.ktor:ktor-serialization-kotlinx-xml:${rootProject.extra["ktor_version"]}")   //Serialization XML

    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.21")           //Multi Platform Serialization
}