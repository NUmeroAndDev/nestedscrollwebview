plugins {
    id("buildlogic.primitive.androidlibrary")
    id("buildlogic.primitive.kotlin")
}

android {
    namespace = "jp.numero.nestedscrollwebview"
}

dependencies {
    implementation(libs.androidxCore)
}