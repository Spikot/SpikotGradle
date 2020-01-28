package kr.heartpattern.spikotgradle

import org.gradle.api.Project
import org.gradle.kotlin.dsl.get

fun Project.spikot(configuration: SpikotExtension.() -> Unit) {
    val extension = extensions["spikot"] as SpikotExtension
    extension.configuration()
}

open class SpikotExtension {
    val output: String? = null
}