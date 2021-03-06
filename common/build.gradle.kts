import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0-alpha3"
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
        val kodein = "7.7.0"
        val kotlinx_serialization_json = "1.2.2"
        val napierVersion = "2.1.0"
        val decompose = "0.3.1"
        val essenty = "0.1.3"
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api("org.kodein.di:kodein-di:$kodein")
                api("org.kodein.di:kodein-di-framework-compose:$kodein")
                implementation("com.arkivanov.essenty:lifecycle:$essenty")
                implementation("com.arkivanov.essenty:parcelable:$essenty")
                api("com.arkivanov.decompose:decompose:$decompose")
                api("com.arkivanov.decompose:extensions-compose-jetbrains:$decompose")
                implementation(project(mapOf("path" to ":commonDomain")))
                api("io.github.aakira:napier:$napierVersion")

            }
        }
        val commonTest by getting
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.3.1")
                api("androidx.core:core-ktx:1.6.0")

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
                implementation(compose.desktop.currentOs)
            }
        }
        val desktopTest by getting


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
dependencies {
    implementation(project(mapOf("path" to ":commonDomain")))
}

