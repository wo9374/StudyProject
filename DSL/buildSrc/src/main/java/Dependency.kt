import Versions.dataStore
import Versions.glide
import Versions.hilt
import Versions.room
import Versions.ktor
import Versions.okHttp
import Versions.orBit
import Versions.retrofit
import Versions.protoBuf
import Versions.livedata
import Versions.viewmodel

object Versions {
    const val hilt = "2.49"

    /**
    * Use build.gradle (:project)
    * id("com.google.devtools.ksp") version Versions.ksp apply false
    *
    * Use build.gradle (:app) plugins
    * id("com.google.devtools.ksp")
    * */
    const val ksp = "1.9.21-1.0.15"

    const val dataStore = "1.0.0"
    const val room = "2.6.1"
    
    const val livedata = "2.6.2"
    const val viewmodel = "2.6.2"

    const val ktor = "2.3.6"
    const val retrofit = "2.9.0"
    const val okHttp = "4.11.0"
    const val orBit = "6.1.0"

    const val protoBufPlugin = "0.9.1"
    const val protoBuf = "3.25.1"
    const val glide = "4.14.2"
}

object Lib {

    object Android {
        const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
        const val activity = "androidx.activity:activity-ktx:1.8.1"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$livedata"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$viewmodel"
    }
    object DataStore {
        const val preferencesStore = "androidx.datastore:datastore-preferences:$dataStore"
        const val protoStore = "androidx.datastore:datastore:$dataStore"

        //Android 종속성 없이 사용 (Multi 모듈용)
        const val preferencesCore = "androidx.datastore:datastore-preferences-core:$dataStore"
        const val protoCore = "androidx.datastore:datastore-core:1.0.0:$dataStore"
    }

    object Room {
        const val core = "androidx.room:room-runtime:$room"
        const val compiler = "androidx.room:room-compiler:$room"
        const val ktx = "androidx.room:room-ktx:$room"
    }

    object Ktor {
        const val core = "io.ktor:ktor-client-android:$ktor"
        const val cio = "io.ktor:ktor-client-cio:$ktor"                          //JVM, Android 및 Native 플랫폼에서 사용할 수 있는 완전 비동기식 코루틴 기반 엔진
        const val logging = "io.ktor:ktor-client-logging-jvm:$ktor"              //HTTP Request을 로깅하기 위해 사용
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:$ktor"  //직렬화/역직렬화를 위한 ContentNegotiation
        const val serializationJson = "io.ktor:ktor-serialization-kotlinx-json:$ktor"    //Serialization json
        const val serializationXml = "io.ktor:ktor-serialization-kotlinx-xml:$ktor"      //Serialization XML
    }

    object Serialization {
        const val json =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.21"          //Multi Platform Serialization
    }

    object Retrofit {
        const val core = "com.squareup.retrofit2:retrofit:$retrofit"
        const val gson = "com.squareup.retrofit2:converter-gson:$retrofit"       //Gson 처리
        const val jaxb = "com.squareup.retrofit2:converter-jaxb:$retrofit"       //XML 처리
        const val scalars = "com.squareup.retrofit2:converter-scalars:$retrofit" //String 처리
    }

    object OkHttp {
        const val core = "com.squareup.okhttp3:okhttp:$okHttp"
        const val interceptor = "com.squareup.okhttp3:logging-interceptor:$okHttp" //요청, 응답 정보 기록
    }

    object Orbit {
        const val core = "org.orbit-mvi:orbit-core:$orBit"
        const val viewModel = "org.orbit-mvi:orbit-viewmodel:$orBit"
    }

    object Dagger {
        object Hilt {
            const val core = "com.google.dagger:hilt-android:$hilt"
            const val compiler = "com.google.dagger:hilt-android-compiler:$hilt"
        }
    }

    object Kotlin {
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"
    }

    object Google {

        object ProtoBuf {
            const val kotlin = "com.google.protobuf:protobuf-kotlin-lite:$protoBuf"
            const val java = "com.google.protobuf:protobuf-javalite:$protoBuf"
            const val protoCompiler = "com.google.protobuf:protoc:$protoBuf"        //protobuf protoc { artifacit = this }
        }

        object Glide {
            const val core = "com.github.bumptech.glide:glide:$glide"
            const val compilerKapt = "com.github.bumptech.glide:compiler:$glide"
            const val compilerKsp = "com.github.bumptech.glide:ksp:$glide"
        }
    }
}