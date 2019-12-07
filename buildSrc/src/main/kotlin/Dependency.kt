object Build {

    object Version {
        const val kotlin = "1.3.61"
        const val buildTools = "28.0.3"
    }

    object Dependency {
        const val androidGradle = "com.android.tools.build:gradle:3.5.3"
        const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
        const val navigationSafeArgs =
            "androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0"
    }

    object Plugin {
        const val androidApplication = "com.android.application"
        const val androidLibrary = "com.android.library"
        const val kotlinAndroid = "kotlin-android"
        const val kotlinAndroidExtensions = "kotlin-android-extensions"
        const val kotlinKapt = "kotlin-kapt"
        const val navigationKotlinSafeArgs = "androidx.navigation.safeargs.kotlin"
    }
}

object AndroidSdk {
    const val min = 23
    const val compile = 29
    const val target = compile
}

object Library {
    object Kotlin {
        const val std = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Build.Version.kotlin}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2"
    }

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.1.0"
        const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.1.0"
        const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-rc02"
        const val lifecycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0-rc02"
        const val appCompat = "androidx.appcompat:appcompat:1.1.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:2.1.0"
        const val navigationUi = "androidx.navigation:navigation-ui-ktx:2.1.0"
        const val paging = "androidx.paging:paging-runtime:2.1.0"
        const val browser = "androidx.browser:browser:1.0.0"
    }

    object Koin {
        private const val version = "2.0.1"

        const val core = "org.koin:koin-core:$version"
        const val android = "org.koin:koin-android:$version"
        const val androidScope = "org.koin:koin-androidx-scope:$version"
        const val androidViewModel = "org.koin:koin-androidx-viewmodel:$version"
    }

    object Coil {
        private const val version = "0.8.0"

        const val coil = "io.coil-kt:coil:$version"
        const val gif = "io.coil-kt:coil-gif:$version"
    }

    object Squareup {
        private const val versionOkhttp = "4.2.1"
        private const val versionMoshi = "1.8.0"
        private const val versionRetrofit = "2.6.2"

        const val okhttp = "com.squareup.okhttp3:okhttp:$versionOkhttp"
        const val logginInterceptor = "com.squareup.okhttp3:logging-interceptor:$versionOkhttp"
        const val moshi = "com.squareup.moshi:moshi:$versionMoshi"
        const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:$versionMoshi"
        const val moshiKotlinCodgen = "com.squareup.moshi:moshi-kotlin-codegen:$versionMoshi"
        const val retrofit = "com.squareup.retrofit2:retrofit:$versionRetrofit"
        const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:$versionRetrofit"
    }

    const val exoPlayer = "com.google.android.exoplayer:exoplayer:2.10.8"
    const val timber = "com.jakewharton.timber:timber:4.7.1"
}