package jp.numero.nestedwebview.buildlogic.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class KotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<KotlinAndroidProjectExtension> {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        listOf(
                            "-Xcontext-parameters",
                        ),
                    )
                }
            }
            dependencies {
                implementation(libs.findLibrary("kotlinxCoroutines"))
            }
        }
    }
}