import com.google.protobuf.gradle.id

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.protobuf")
}

android {
    namespace = "com.ljb.datastore"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ljb.datastore"
        minSdk = 28
        targetSdk = 34
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

protobuf {
    protoc {
        artifact = Lib.Google.ProtoBuf.protoCompiler
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins{
                id("java"){ option("lite") }
                //id("kotlin"){ option("lite") }
            }
            /*task.plugins {
                create("grpc") {
                    option("lite")
                }
            }*/
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(Lib.DataStore.preferencesStore)

    implementation(Lib.Google.ProtoBuf.java)
    //implementation(Lib.Google.ProtoBuf.kotlin)

    implementation(Lib.Activity.ktx)
}