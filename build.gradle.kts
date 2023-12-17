// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    kotlin("jvm") version "1.9.10" apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    kotlin("plugin.serialization") version "1.9.10"
}

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.9.10"))
    }
}
true // Needed to make the Suppress annotation work for the plugins block

