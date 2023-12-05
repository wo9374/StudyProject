import Versions.ktor

object Versions {
    const val ktor = "2.3.6"
}

object Lib{
    object Ktor{
        const val core = "io.ktor:ktor-client-android:${ktor}"
        const val cio = "io.ktor:ktor-client-cio:${ktor}"                          //JVM, Android 및 Native 플랫폼에서 사용할 수 있는 완전 비동기식 코루틴 기반 엔진
        const val logging = "io.ktor:ktor-client-logging-jvm:${ktor}"              //HTTP Request을 로깅하기 위해 사용
        const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:${ktor}"  //직렬화/역직렬화를 위한 ContentNegotiation

        const val serializationJson = "io.ktor:ktor-serialization-kotlinx-json:${ktor}"    //Serialization json
        const val serializationXml = "io.ktor:ktor-serialization-kotlinx-xml:${ktor}"      //Serialization XML
    }

    object Serialization{
        const val json = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.21"          //Multi Platform Serialization
    }
}

