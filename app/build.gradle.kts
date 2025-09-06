import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.text.DecimalFormat

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.devtools.ksp) // Or your alias if different
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.walkmansit.realworld"
    compileSdk = 36

    defaultConfig {

        applicationId = "com.walkmansit.realworld"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

//        buildConfigField("String", "API_KEY", "\"default_key\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
//            buildConfigField("String", "API_URL", "\"${project.properties["api.url"]}\"")
        }
        release {
//            buildConfigField("String", "API_URL", "\"${project.properties["api.url"]}\"")
            isMinifyEnabled = true // must be true for R8 to run
            isShrinkResources = true // optional
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
        buildConfig = true // ensure generation
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // Configure APK output names
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val outputFileName = "app-${variant.name}-${variant.versionName}.apk"
                output.outputFileName = outputFileName
            }
    }
}

detekt {
    toolVersion = "1.23.8"
    config.from("$rootDir/detekt.yml")
    buildUponDefaultConfig = false
    allRules = false
}

ktlint {
    debug.set(false)
    android.set(true)
    ignoreFailures.set(false)
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.HTML)
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.paging.common.android)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.constraintlayout.compose)

    // /hilt
    implementation(libs.hilt.android.core)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Material icons
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)

    // FlowMvi
    implementation(libs.flowmvi.core)
    implementation(libs.flowmvi.test)
    implementation(libs.flowmvi.compose)
    implementation(libs.flowmvi.android)
    implementation(libs.flowmvi.savedstate)
    implementation(libs.flowmvi.debugger)
    implementation(libs.flowmvi.essenty)
    implementation(libs.flowmvi.essenty.compose)

    // Immutable collections
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // LeakCanary
    debugImplementation(libs.leakcanary.android)

    // detect
    detektPlugins(libs.detekt.formatting)

//    ktlintRuleset(libs.rules.ktlint)

    // Routing and args
//    ksp(libs.processor.ksp)
//    kapt(libs.processor.kapt)
//    implementation(libs.core)
//    implementation(libs.accompanist.navigation)
}

// tasks.getByPath("preBuild").dependsOn("ktlintFormat")
tasks.check {
    dependsOn(tasks.detekt)
    dependsOn(tasks.ktlintCheck)
}

abstract class SuperCleanTask : DefaultTask() {
    // Directories to clean
    var dirsToDelete: List<File> = emptyList()

    @TaskAction
    fun clean() {
        dirsToDelete.forEach { dir ->
            if (dir.exists()) {
                dir.deleteRecursively()
                println("✅ Deleted: ${dir.absolutePath}")
            } else {
                println("⚠️ Directory not found: ${dir.absolutePath}")
            }
        }
        println("🎉 Super clean completed!")
    }
}

// Task to clean build cache thoroughly
tasks.register<SuperCleanTask>("superClean") {
    group = "build"
    description = "Cleans build artifacts, Gradle caches, and IDE caches"

    dirsToDelete =
        listOf(
            rootProject.layout.buildDirectory.asFile,
            rootProject.layout.projectDirectory
                .dir(".gradle/caches")
                .asFile,
            rootProject.layout.projectDirectory
                .dir(".idea/caches")
                .asFile,
        ) as List<File>
}

abstract class ApkSizeTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val apkDir: DirectoryProperty

    @TaskAction
    fun printApkSizes() {
        val dir = apkDir.asFile.get()
        if (!dir.exists()) {
            println("❌ No APKs found in $dir. Build first with ./gradlew assembleDebug or assembleRelease")
            return
        }

        val format = DecimalFormat("#,##0.00")
        dir
            .walkTopDown()
            .filter { it.isFile && it.extension == "apk" }
            .forEach { apk ->
                val sizeBytes = apk.length()
                val sizeMB = sizeBytes.toDouble() / (1024 * 1024)
                println("📦 APK: ${apk.name} → ${format.format(sizeMB)} MB")
            }
    }
}

abstract class ModuleArtifactSizeTask : DefaultTask() {
    @get:Input
    abstract val moduleName: Property<String>

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val buildDirProperty: DirectoryProperty

    @TaskAction
    fun printArtifactSizes() {
        val format = DecimalFormat("#,##0.00")
        val module = moduleName.get()
        val buildDir: File = buildDirProperty.asFile.get()

        if (!buildDir.exists()) {
            println("❌ No build directory found for module '$module': $buildDir")
            return
        }

        // APKs
        val apkFiles =
            buildDir
                .resolve("outputs/apk")
                .takeIf { it.exists() }
                ?.walkTopDown()
                ?.filter { it.isFile && it.extension == "apk" }
                ?.toList()
                ?: emptyList()

        // AARs (Android library)
        val aarFiles =
            buildDir.resolve("outputs/aar").listFiles { f -> f.isFile && f.extension == "aar" } ?: emptyArray()

        // JARs (Java/Kotlin library)
        val jarFiles = buildDir.resolve("libs").listFiles { f -> f.isFile && f.extension == "jar" } ?: emptyArray()

        if (apkFiles.isEmpty() && aarFiles.isEmpty() && jarFiles.isEmpty()) {
            println("⚠️ No APK, AAR, or JAR files found for module '$module'")
            return
        }

        // Print APK sizes
        apkFiles.forEach { apk ->
            val sizeMB = apk.length().toDouble() / (1024 * 1024)
            println("📦 [$module] APK: ${apk.name} → ${format.format(sizeMB)} MB")
        }

        // Print AAR sizes
        aarFiles.forEach { aar ->
            val sizeKB = aar.length().toDouble() / 1024
            println("📦 [$module] AAR: ${aar.name} → ${format.format(sizeKB)} KB")
        }

        // Print JAR sizes
        jarFiles.forEach { jar ->
            val sizeKB = jar.length().toDouble() / 1024
            println("📦 [$module] JAR: ${jar.name} → ${format.format(sizeKB)} KB")
        }
    }
}

tasks.register<ApkSizeTask>("apkSize") {
    group = "reporting"
    description = "Prints the size of the generated APK(s)."
    apkDir.set(layout.buildDirectory.dir("outputs/apk"))
}

tasks.register("allArtifactsSize") {
    group = "reporting"
    description = "Prints sizes of all APKs, AARs, and JARs for all modules"

    // Depends on all per-module size tasks
    dependsOn(
        subprojects.map { subproject ->
            tasks.named<ModuleArtifactSizeTask>("${subproject.name}ArtifactSize")
        },
    )
}
