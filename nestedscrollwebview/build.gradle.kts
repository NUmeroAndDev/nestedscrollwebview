plugins {
    id("buildlogic.primitive.androidlibrary")
    id("maven-publish")
}

android {
    namespace = "com.github.numeroanddev.nestedscrollwebview"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidxCore)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.getByName("release"))

                groupId = "com.github.numeroanddev.nestedscrollwebview"
                artifactId = "nestedscrollwebview"
                version = "0.1"
            }
        }
    }
}