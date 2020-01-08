import orwir.gradle.extension.stringField
import orwir.gradle.extension.unitTestsLibraries

plugins {
    id(Build.Plugin.library)
    id(Build.Plugin.kotlin_android)
    id(Build.Plugin.kotlin_android_extensions)
    id(Build.Plugin.simple_android)
}

android {
    defaultConfig {
        stringField("REDDIT_URL_BASIC", "https://www.reddit.com")
        stringField("REDDIT_URL_OAUTH", "https://oauth.reddit.com")
    }
}

dependencies {
    // Kotlin
    api(Library.Kotlin.std)
    api(Library.Kotlin.coroutines)
    api(Library.Kotlin.reflect)

    // Androidx
    api(Library.AndroidX.core)
    api(Library.AndroidX.lifecycle_runtime)
    api(Library.AndroidX.lifecycle_livedata)
    api(Library.AndroidX.lifecycle_extensions)
    api(Library.AndroidX.lifecycle_viewModel)

    // DI
    api(Library.Koin.core)
    api(Library.Koin.android)
    api(Library.Koin.android_scope)
    api(Library.Koin.android_viewmodel)

    // Image Loader
    api(Library.Coil.base)
    implementation(Library.Coil.gif)

    // Logger
    api(Library.timber)

    // Network & Json
    implementation(Library.Squareup.okhttp)
    implementation(Library.Squareup.moshi)
    implementation(Library.Squareup.moshi_kotlin)
    implementation(Library.Squareup.retrofit)

    unitTestsLibraries()
}