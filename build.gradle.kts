plugins {
    kotlin("jvm").version("1.3.61")
    id("maven-publish")
    id("com.gradle.plugin-publish").version("0.10.1")
}

group = "kr.heartpattern"
version = "4.0.2"

repositories {
    maven("https://maven.heartpattern.kr/repository/maven-public/")
}

val kotlinVersion = "1.3.61"

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
    implementation(gradleKotlinDsl())
    implementation(kotlin("gradle-plugin", kotlinVersion))

    runtime(kotlin("annotation-processing-gradle", kotlinVersion))
    runtime(kotlin("serialization", kotlinVersion))
}

pluginBundle {
    website = "https://github.com/Spikot/SpikotGradle"
    vcsUrl = "https://github.com/Spikot/SpikotGradle"
    description = "Gradle plugin for Spikot Framework"
    tags = listOf("bukkit", "spikot")
    plugins {
        create("SpikotPlugin") {
            id = "kr.heartpattern.spikot"
            displayName = "SpikotGradle"
        }
    }
}