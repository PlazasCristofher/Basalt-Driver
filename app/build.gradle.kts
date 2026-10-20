plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.mio.plugin.renderer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.d4urw.basaltdriver"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "0.1.0"

        // Nombre que muestra Zalith para este driver
        manifestPlaceholders["driver"] = "Basalt Driver (PanVK-kbase RC3)"
        ndk { abiFilters += "arm64-v8a" }
    }

    buildTypes {
        release {
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
}
