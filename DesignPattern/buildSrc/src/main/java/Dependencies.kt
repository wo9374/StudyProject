import Versions.okHttp
import Versions.orBit
import Versions.retrofit

object Versions {
    const val retrofit = "2.9.0"
    const val okHttp = "4.11.0"
    const val orBit = "6.1.0"
}

object Lib{
    object Retrofit{
        const val core = "com.squareup.retrofit2:retrofit:$retrofit"
        const val gson = "com.squareup.retrofit2:converter-gson:$retrofit"       //Gson 처리
        const val jaxb = "com.squareup.retrofit2:converter-jaxb:$retrofit"       //XML 처리
        const val scalars = "com.squareup.retrofit2:converter-scalars:$retrofit" //String 처리
    }

    object OkHttp{
        const val core = "com.squareup.okhttp3:okhttp:$okHttp"
        const val interceptor = "com.squareup.okhttp3:logging-interceptor:$okHttp" //요청, 응답 정보 기록
    }

    object Activity{
        const val ktx = "androidx.activity:activity-ktx:1.8.1"
    }

    object Orbit{
        const val core = "org.orbit-mvi:orbit-core:$orBit"
        const val viewModel = "org.orbit-mvi:orbit-viewmodel:$orBit"
    }
}

