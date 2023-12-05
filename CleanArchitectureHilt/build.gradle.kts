plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("com.google.devtools.ksp") version Versions.ksp apply false

    id("com.google.dagger.hilt.android") version Versions.hilt apply false
}