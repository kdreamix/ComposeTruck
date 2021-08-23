import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.5.20"
    id("org.jetbrains.compose") version "0.3.1"
    id("com.android.library")
    id("kotlin-android-extensions")
}

group = "me.kit"
version = "1.0"



repositories {
    google()
}

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        val ktor_version = "1.6.2"
        val logack_version = "1.2.5"
        val kodein = "7.5.0"
        val kotlinx_serialization_json = "1.2.2"
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_json")
                api("io.ktor:ktor-client-core:$ktor_version")
                api("io.ktor:ktor-client-cio:$ktor_version")
                api("io.ktor:ktor-client-serialization:$ktor_version")
                api("ch.qos.logback:logback-classic:$logack_version")
                api("io.ktor:ktor-client-logging:$ktor_version")
                api("org.kodein.di:kodein-di:$kodein")
                api("org.kodein.di:kodein-di-framework-compose:$kodein")
            }
        }
        val commonTest by getting
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.2.0")
                api("androidx.core:core-ktx:1.3.1")
                implementation("io.ktor:ktor-client-okhttp:$ktor_version")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13")
            }
        }
        val desktopMain by getting
        val desktopTest by getting


    }
}

android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
    }
}