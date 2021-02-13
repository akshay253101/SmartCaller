package com.beetlestance.smartcaller.buildsrc

object SmartCaller {
    const val minSdkVersion: Int = 21
    const val targetSdkVersion: Int = 30
    const val compileSdkVersion: Int = 30
    const val applicationId: String = "com.beetlestance.smartcaller"
    const val versionCode: Int = 1
    const val versionName: String = "0.1"
}

object Libs {

    const val androidGradlePlugin: String = "com.android.tools.build:gradle:4.1.1"

    object Test {
        const val junit: String = "junit:junit:4.13.1"
        const val robolectric: String = "org.robolectric:robolectric:4.3.1"
        const val mockK: String = "io.mockk:mockk:1.9.3"
    }

    object AndroidX {
        const val appcompat: String = "androidx.appcompat:appcompat:1.3.0-alpha02"
        const val coreKtx: String = "androidx.core:core-ktx:1.5.0-alpha05"
        const val paging = "androidx.paging:paging-runtime-ktx:3.0.0-alpha11"

        object Test {
            private const val version = "1.3.0"
            const val core: String = "androidx.test:core:$version"
            const val runner: String = "androidx.test:runner:$version"
            const val rules: String = "androidx.test:rules:$version"
            const val junit: String = "androidx.test.ext:junit:1.1.2"
            const val espressoCore: String = "androidx.test.espresso:espresso-core:3.3.0"
        }

        object Lifecycle {
            private const val version = "2.3.0-rc01"
            const val extensions: String = "androidx.lifecycle:lifecycle-extensions:$version"
            const val viewmodelKtx: String = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val livedataKtx: String = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val compiler: String = "androidx.lifecycle:lifecycle-compiler:$version"
            const val common: String = "androidx.lifecycle:lifecycle-common-java8:$version"
        }

        object Navigation {
            private const val version = "2.3.2"
            const val fragmentKtx: String = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx: String = "androidx.navigation:navigation-ui-ktx:$version"
        }

        object Room {
            private const val version = "2.3.0-alpha04"
            const val common: String = "androidx.room:room-common:$version"
            const val runtime: String = "androidx.room:room-runtime:$version"
            const val compiler: String = "androidx.room:room-compiler:$version"

            // optional - Kotlin Extensions and Coroutines support for Room
            const val ktx: String = "androidx.room:room-ktx:$version"

            // optional - Test helpers
            const val testing: String = "androidx.room:room-testing:$version"
        }
    }

    object Google {
        const val material: String = "com.google.android.material:material:1.3.0-beta01"
        const val gson: String = "com.google.code.gson:gson:2.8.6"

        object PlayServices {
            const val gmsGoogleServices: String = "com.google.gms:google-services:4.3.4"
        }

        object Firebase {
            const val crashlytics: String = "com.google.firebase:firebase-crashlytics:17.3.0"
            const val crashlyticsGradle: String =
                "com.google.firebase:firebase-crashlytics-gradle:2.4.1"
        }
    }

    object Coil {
        private const val version = "1.1.0"
        const val coil: String = "io.coil-kt:coil:$version"
    }

    object Dagger {
        private const val version = "2.30.1"
        const val dagger: String = "com.google.dagger:dagger:$version"
        const val compiler: String = "com.google.dagger:dagger-compiler:$version"
        const val processor = "com.google.dagger:dagger-android-processor:$version"
        const val support = "com.google.dagger:dagger-android-support:$version"
    }

    object Kotlin {
        const val version: String = "1.4.21"
        const val stdlib: String = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin: String = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
    }

    object Coroutines {
        private const val version = "1.4.2"
        const val core: String = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android: String = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test: String = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }
}
