// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin ("jvm") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.19" apply false

    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}