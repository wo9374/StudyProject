import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("kotlin-kapt")
}

android {
    namespace = "com.ljb.designpattern"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ljb.designpattern"
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
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(Lib.Retrofit.core)
    implementation(Lib.Retrofit.gson)
    implementation(Lib.OkHttp.interceptor)

    implementation(Lib.Activity.ktx)
    
    implementation(Lib.Orbit.core)
    implementation(Lib.Orbit.viewModel)
}