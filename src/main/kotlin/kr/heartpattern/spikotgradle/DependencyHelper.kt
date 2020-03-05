package kr.heartpattern.spikotgradle

import org.gradle.api.artifacts.dsl.DependencyHandler
import java.util.regex.Pattern

private val onlyVersion = Pattern.compile("""^\d+\.\d+(\.\d+)?$""")
private val releaseVersion = Pattern.compile("""^\d+\.\d+(\.\d+)?-R\d+\.\d+$""")
private val preVersion = Pattern.compile("""^\d+\.\d+(\.\d+)?-pre\d+$""")

fun DependencyHandler.bukkitApi(version: String) {
    val fullVersion = completeVersion(version)
    add("compileOnly", "org.bukkit:bukkit:$fullVersion")
}

fun DependencyHandler.spigotApi(version: String) {
    val fullVersion = completeVersion(version)
    add("compileOnly", "org.spigotmc:spigot-api:$fullVersion")
}

fun DependencyHandler.paperApi(version: String) {
    val fullVersion = completeVersion(version)
    add("compileOnly", "com.destroystokyo.paper:paper-api:$fullVersion")
}

fun DependencyHandler.spikot(version: String){
    add("compileOnly", "kr.heartpattern:Spikot:$version")
}

private fun completeVersion(version: String): String {
    val regularized = if (version.endsWith("-SNAPSHOT"))
        version.substring(0..(version.length - 10))
    else
        version

    return when{
        onlyVersion.matcher(regularized).matches() -> "$regularized-R0.1"
        releaseVersion.matcher(regularized).matches() -> regularized
        preVersion.matcher(regularized).matches() -> regularized
        else -> throw IllegalArgumentException("Cannot understand version notation \"$version\"")
    } + "-SNAPSHOT"
}