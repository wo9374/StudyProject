// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

    kotlin("plugin.serialization") version "1.9.21" apply false
    //kotlin("jvm") version "1.9.21" // 멀티플랫폼이나 다른 플러그인에 serialization 사용할 때 지정
}