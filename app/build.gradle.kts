import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

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
            isMinifyEnabled = true
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

// Task to clean build cache thoroughly
tasks.register("superClean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
    delete("${rootProject.rootDir}/.gradle/caches")
    delete("${rootProject.rootDir}/.idea/caches")
    doLast {
        println("Super clean completed!")
    }
}

// Task to check for dependency updates using proper exec
tasks.register("dependencyUpdates") {
    doLast {
        val process = ProcessBuilder(
            "${rootProject.projectDir}/gradlew",
            "dependencyUpdates",
            "-Drevision=release"
        ).redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        process.waitFor()
        val output = process.inputStream.bufferedReader().readText()
        println("Dependency Updates:\n$output")
    }
}