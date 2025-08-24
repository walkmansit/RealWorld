// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application)  apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.devtools.ksp) apply false// Or your alias if different
    alias(libs.plugins.google.dagger.hilt.android) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false

//    id("com.google.dagger.hilt.android") version "2.57" apply false
    kotlin("jvm") version "2.2.10"
//    id("com.google.devtools.ksp") version "2.2.10-2.0.2" apply false
}