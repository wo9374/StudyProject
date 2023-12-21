import Versions.glide

object Versions {

    /**
    * Use build.gradle (:project)
    * id("com.google.devtools.ksp") version Versions.ksp apply false
    *
    * Use build.gradle (:app) plugins
    * id("com.google.devtools.ksp")
    * */
    const val ksp = "1.9.21-1.0.15"

    const val glide = "4.14.2"
}

object Lib {

    object Android {
        const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    }
    object Google {

        object Glide {
            const val core = "com.github.bumptech.glide:glide:$glide"
            const val compilerKapt = "com.github.bumptech.glide:compiler:$glide"
            const val compilerKsp = "com.github.bumptech.glide:ksp:$glide"
        }
    }
}