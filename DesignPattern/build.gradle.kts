// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

buildscript {
    extra.apply {
        set("retrofit_version", "2.9.0")
        set("okhttp_version", "4.11.0")
    }
}