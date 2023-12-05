import Versions.liveData
import Versions.room

object Versions {
    const val ksp = "1.9.21-1.0.15"

    const val room = "2.6.1"

    const val liveData = "2.6.2"
}

object Lib{

    object Room{
        const val core = "androidx.room:room-runtime:$room"
        const val compiler = "androidx.room:room-compiler:$room"
        const val ktx = "androidx.room:room-ktx:$room"
    }

    object LiveData{
        const val ktx = "androidx.lifecycle:lifecycle-livedata-ktx:$liveData"
    }

}

