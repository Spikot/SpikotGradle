package kr.heartpattern.spikotgradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

class SpikotGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.buildscript.repositories.maven { config ->
            config.url = URI("https://maven.heartpattern.kr/repository/maven-public/")
        }
        project.repositories.maven { config ->
            config.url = URI("https://maven.heartpattern.kr/repository/maven-public/")
        }

        val extension = project.extensions.create("spikot", SpikotExtension::class.java)

        project.plugins.apply("org.jetbrains.kotlin.jvm")
        project.plugins.apply("org.jetbrains.kotlin.kapt")
        project.plugins.apply("org.jetbrains.kotlin.plugin.serialization")

        with(project.dependencies) {
            add("compileOnly", "kr.heartpattern:Spikot:4.0.0-SNAPSHOT")
            add("compileOnly", "org.spigotmc:plugin-annotations:1.2.2-SNAPSHOT")
            add("kapt", "org.spigotmc:plugin-annotations:1.2.2-SNAPSHOT")
        }

        project.tasks.withType(KotlinCompile::class.java) { config ->
            config.kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-XXLanguage:+InlineClasses",
                    "-Xuse-experimental=kotlin.Experimental"
                )
            }
        }

        project.afterEvaluate { prj ->
            prj.dependencies.add("kapt", "kr.heartpattern:SpikotClassLocator:4.0.0-SNAPSHOT")
            prj.tasks.create("createPlugin", Jar::class.java) { task ->
                task.archiveFileName.set(extension.output ?: "${prj.name}-Plugin.jar")
                task.from(project.configurations.getByName("compile").map {
                    if (it.isDirectory)
                        it
                    else
                        prj.zipTree(it)
                })
                task.with(prj.tasks.getByName("jar") as Jar)
            }
        }
    }
}