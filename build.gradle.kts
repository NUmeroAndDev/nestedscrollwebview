buildscript {
    dependencies {
        // ref: https://github.com/google/dagger/issues/3068
        classpath("com.squareup:javapoet:1.13.0")
        classpath(libs.kotlinGradlePlugin)
        classpath(libs.kspPlugin)
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.composeCompiler) apply false
}