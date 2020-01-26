package kr.heartpattern.spikotgradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar

class SpikotGradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.repositories {
            maven {
                url 'https://maven.heartpattern.kr/repository/maven-public'
            }
        }

        project.buildscript.repositories {
            maven {
                url 'https://maven.heartpattern.kr/repository/maven-public'
            }
        }

        def extension = project.extensions.create('spikot', SpikotExtension)
        extension.output = "${project.name}-Plugin.jar"

        project.pluginManager.apply('org.jetbrains.kotlin.jvm')
        project.pluginManager.apply('org.jetbrains.kotlin.kapt')
        project.pluginManager.apply('org.jetbrains.kotlin.plugin.serialization')

        project.dependencies {
            compileOnly("kr.heartpattern:Spikot:4.0.0-SNAPSHOT") {
                exclude group: 'org.bukkit', module: 'bukkit'
            }
            compileOnly("org.spigotmc:plugin-annotations:1.2.2-SNAPSHOT") {
                exclude group: 'org.bukkit', module: 'bukkit'
            }

            kapt "org.spigotmc:plugin-annotations:1.2.2-SNAPSHOT"
        }

        project.compileKotlin {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs += '-XXLanguage:+InlineClasses'
                freeCompilerArgs += '-Xuse-experimental=kotlin.Experimental'
            }
        }

        project.compileTestKotlin {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }

        project.afterEvaluate { prj ->
            prj.dependencies {
                kapt "kr.heartpattern:SpikotClassLocator:4.0.0-SNAPSHOT"
            }
            project.task(type: Jar, 'createPlugin') {
                archiveFileName = extension.output == null ? "${project.name}-Plugin.jar" : extension.output
                from {
                    project.configurations.compile.collect { it.isDirectory() ? it : zipTree(it) }
                }
                with project.jar
            }
        }
    }
}
