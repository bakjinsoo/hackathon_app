// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google() // Ensure you have this repository
        mavenCentral() // or jcenter() if you still use it
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2") // or your project's gradle version
        classpath("com.google.gms:google-services:4.4.2")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}
//allprojects {
//    repositories {
//        //google()
//        mavenCentral() // or jcenter()
//    }
//}
plugins {
    id("com.android.application") version "8.1.1" apply false
}