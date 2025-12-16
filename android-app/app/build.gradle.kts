android {
    // 其他 android 配置不变...

    signingConfigs {
        create("release") {
            storeFile = file("keystore/autoglm-release.keystore") // 注意：相对 app/ 目录
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
