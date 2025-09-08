import org.gradle.kotlin.dsl.register

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.devtools.ksp) apply false// Or your alias if different
    alias(libs.plugins.google.dagger.hilt.android) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.parcelize) apply false

    kotlin("jvm") version "2.2.10"
    alias(libs.plugins.android.library) apply false
}

abstract class ModuleArtifactSizeTask : DefaultTask() {
    @get:Input
    abstract val moduleName: Property<String>

    @get:InputDirectory
    @get:Optional
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val buildDirProperty: DirectoryProperty

    @get:OutputFile
    abstract val reportFile: RegularFileProperty

    init {
        // Always run to refresh sizes
        outputs.upToDateWhen { false }
    }

    @TaskAction
    fun printArtifactSizes() {
        val report = StringBuilder()
        val module = moduleName.get()
        val buildDir = buildDirProperty.asFile.orNull

        fun formatSize(bytes: Long): String {
            val kb = bytes / 1024.0
            val mb = kb / 1024.0
            return when {
                mb >= 1 -> String.format("%.2f MB", mb)
                kb >= 1 -> String.format("%.2f KB", kb)
                else -> "$bytes B"
            }
        }

        if (buildDir == null || !buildDir.exists()) {
            report.append("⚠️ Build directory does not exist for $module\n")
        } else {
            val apkFiles = buildDir.resolve("outputs/apk")
                .takeIf { it.exists() }
                ?.walkTopDown()
                ?.filter { it.isFile && it.extension == "apk" }
                ?.toList()
                .orEmpty()

            val aarFiles = buildDir.resolve("outputs/aar").listFiles { f -> f.isFile && f.extension == "aar" } ?: emptyArray()
            val jarFiles = buildDir.resolve("libs").listFiles { f -> f.isFile && f.extension == "jar" } ?: emptyArray()

            if (apkFiles.isEmpty() && aarFiles.isEmpty() && jarFiles.isEmpty()) {
                report.append("⚠️ No artifacts found for $module\n")
            } else {
                apkFiles.forEach { report.append("📦 [$module] APK: ${it.name} → ${formatSize(it.length())}\n") }
                aarFiles.forEach { report.append("📦 [$module] AAR: ${it.name} → ${formatSize(it.length())}\n") }
                jarFiles.forEach { report.append("📦 [$module] JAR: ${it.name} → ${formatSize(it.length())}\n") }
            }
        }

        // Print immediately
        println(report.toString())

        // Save report for reference if desired
        reportFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText(report.toString())
        }
    }
}

subprojects {
    tasks.register<ModuleArtifactSizeTask>("${name}ArtifactSize") {
        moduleName.set(name)
        buildDirProperty.set(layout.buildDirectory)
        reportFile.set(layout.buildDirectory.file("reports/${name}-artifact-size.txt"))
    }
}

tasks.register("allArtifactsSize") {
    group = "reporting"
    description = "Prints sizes of all APKs, AARs, and JARs for all modules"

    // Depend on all subproject artifact tasks by path strings
    dependsOn(subprojects.map { ":${it.name}:${it.name}ArtifactSize" })

    doLast {
        println("✅ All artifact size tasks completed.")
    }
}
