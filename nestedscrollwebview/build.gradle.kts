plugins {
    id("buildlogic.primitive.androidlibrary")
    id("buildlogic.primitive.kotlin")
}

android {
    namespace = "com.github.numeroanddev.nestedscrollwebview"
}

dependencies {
    implementation(libs.androidxCore)
}