plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    // Корутины
    implementation(libs.kotlinx.coroutines.core)

    // Тесты
    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks.test {
    useJUnitPlatform() // если используешь JUnit 5
}



