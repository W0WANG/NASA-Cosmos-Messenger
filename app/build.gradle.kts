import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.nasacosmosmessenger"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.nasacosmosmessenger"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 讀取 local.properties
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        val nasaApiKey = localProperties.getProperty("NASA_API_KEY") ?: ""

        // 注意：buildConfigField 在 kts 裡的語法要確保型別正確
        buildConfigField("String", "NASA_API_KEY", "\"$nasaApiKey\"")
    }

    // 開啟 BuildConfig 功能 (Android Studio 新版本預設可能關閉)
    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    // 網路連線工具
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // JSON 轉物件工具
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // 圖片載入工具
    implementation("io.coil-kt:coil:2.4.0")
}