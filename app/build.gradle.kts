plugins {
    id("buildlogic.conventions.appmodule")
}

android {
    namespace = "com.github.numeroanddev.nestedwebview.app"

    defaultConfig {
        applicationId = "com.github.numeroanddev.nestedwebview.app"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":nestedscrollwebview"))

    implementation(libs.androidxActivity)
    implementation(libs.androidxCore)
    implementation(libs.androidxCoreSplash)
    implementation(libs.androidxProfileInstaller)
    implementation(libs.androidxSwipeRefreshLayout)
    implementation(libs.androidxFragmentCompose)
    implementation(libs.material)
}