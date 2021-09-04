buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
        maven (uri( "https://www.jetbrains.com/intellij-repository/releases"))
        maven (uri("https://jetbrains.bintray.com/intellij-third-party-dependencies"))
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
        classpath("com.android.tools.build:gradle:4.2.2")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.0")
    }
}


group = "me.kit"
version = "1.0"

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}