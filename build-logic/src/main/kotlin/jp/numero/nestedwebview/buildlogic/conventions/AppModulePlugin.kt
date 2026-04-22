package jp.numero.nestedwebview.buildlogic.conventions

import jp.numero.nestedwebview.buildlogic.primitive.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AppModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("buildlogic.primitive.androidapplication")
                apply("buildlogic.primitive.compose")
                apply("buildlogic.primitive.kotlin")
                apply("buildlogic.primitive.test")
            }
            dependencies {
            }
        }
    }
}