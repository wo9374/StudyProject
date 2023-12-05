import Versions.hilt
import Versions.room

object Versions {
    const val hilt ="2.49"

    const val ksp = "1.9.21-1.0.15"

    const val room = "2.6.1"
}

object Lib {

    object Room{
        const val core = "androidx.room:room-runtime:$room"
        const val compiler = "androidx.room:room-compiler:$room"
        const val ktx = "androidx.room:room-ktx:$room"
    }

    object Activity{
        const val ktx = "androidx.activity:activity-ktx:1.8.1"
    }

    object Dagger{
        object Hilt{
            const val core = "com.google.dagger:hilt-android:$hilt"
            const val compiler = "com.google.dagger:hilt-android-compiler:$hilt"
        }
    }

    object Kotlin{
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"
    }
}