plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.autoglm.helper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.autoglm.helper"
        minSdk = 24 // Android 7.0
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    signingConfigs {
        create("release") {
            // 这里路径是相对 android-app/app/ 目录
            storeFile = file("keystore/autoglm-release.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            // debug 默认用系统 debug keystore，不用你额外配置
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")

    // NanoHTTPD - 轻量级 HTTP 服务器
    implementation("org.nanohttpd:nanohttpd:2.3.1")

    // JSON 处理（Android 自带，一般不需要额外声明）
    // implementation("org.json:json:20230227")
}

