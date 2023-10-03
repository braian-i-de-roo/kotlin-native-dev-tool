plugins {
    kotlin("multiplatform") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.0"
}

group = "com.braian"
version = "1.0-SNAPSHOT"

val ktorVersion = "2.3.4"

repositories {
    mavenCentral()
}

kotlin {
    linuxX64("linux").apply {
        binaries {
            executable()
        }
    }
    mingwX64("windows").apply {
        binaries {
            executable()
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
                implementation("com.github.ajalt.clikt:clikt:4.2.0")

                implementation("com.squareup.okio:okio:3.5.0")

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-curl:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
        val linuxMain by getting
        val windowsMain by getting
    }
}
