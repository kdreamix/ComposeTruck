
plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.5.20"
    id("com.android.library")
    id("kotlin-android-extensions")
    id("com.squareup.sqldelight")

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
        val kodein = "7.7.0"
        val kotlinx_serialization_json = "1.2.2"
        val sqldelight = "1.5.0"
        val commonMain by getting {
            dependencies {
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
                api("androidx.appcompat:appcompat:1.3.1")
                api("androidx.core:core-ktx:1.6.0")
                implementation("io.ktor:ktor-client-okhttp:$ktor_version")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_json")
                implementation("com.squareup.sqldelight:android-driver:$sqldelight")

            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13")
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-java:$ktor_version")
                implementation("com.squareup.sqldelight:sqlite-driver:$sqldelight")

            }
        }
        val desktopTest by getting


    }
}

sqldelight {
    database("TruckDatabase") {
        packageName = "com.me.kit"
        sourceFolders = listOf("sqldelight")
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
    }
}