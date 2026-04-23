import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.github.numeroanddev.nestedwebview.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(
            JavaVersion.VERSION_17.toString(),
        )
    }
}

dependencies {
    compileOnly(libs.androidGradlePlugin)
    compileOnly(libs.kotlinGradlePlugin)
}

gradlePlugin {
    plugins {
        // primitives
        register("androidApplication") {
            id = "buildlogic.primitive.androidapplication"
            implementationClass =
                "com.github.numeroanddev.nestedwebview.buildlogic.primitive.AndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "buildlogic.primitive.androidlibrary"
            implementationClass =
                "com.github.numeroanddev.nestedwebview.buildlogic.primitive.AndroidLibraryPlugin"
        }
        register("compose") {
            id = "buildlogic.primitive.compose"
            implementationClass = "com.github.numeroanddev.nestedwebview.buildlogic.primitive.ComposePlugin"
        }
        register("kotlin") {
            id = "buildlogic.primitive.kotlin"
            implementationClass = "com.github.numeroanddev.nestedwebview.buildlogic.primitive.KotlinPlugin"
        }
        register("test") {
            id = "buildlogic.primitive.test"
            implementationClass = "com.github.numeroanddev.nestedwebview.buildlogic.primitive.TestPlugin"
        }

        // conventions
        register("appModule") {
            id = "buildlogic.conventions.appmodule"
            implementationClass = "com.github.numeroanddev.nestedwebview.buildlogic.conventions.AppModulePlugin"
        }
    }
}
