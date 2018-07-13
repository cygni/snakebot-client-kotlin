
import org.jetbrains.kotlin.gradle.dsl.Coroutines

group = "se.cygni.snake"
version = "1.0-SNAPSHOT"

kotlin.experimental.coroutines = Coroutines.ENABLE

plugins {
    application
    kotlin("jvm") version "1.2.31"
}

application {
    mainClassName = "se.cygni.snake.MainKt"
}

val junitPlatformVersion = "1.0.1"
val junitJupiterVersion  = "5.0.2"

dependencies {
    compile(kotlin("stdlib"))
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5")
    compile("com.squareup.okhttp3:okhttp:3.10.0")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
    compile("ch.qos.logback:logback-classic:1.2.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testCompile("io.kotlintest:kotlintest-core:3.0.2")
    testCompile("io.kotlintest:kotlintest-assertions:3.0.2")
    // testCompile("io.kotlintest:kotlintest-runner-junit5:3.0.2")
    // testRuntime("org.junit.platform:junit-platform-launcher:1.0.2")
    // testRuntime("org.junit.jupiter:junit-jupiter-engine:5.0.2")
}

repositories {
    jcenter()
}

tasks {
    // Use the native JUnit support of Gradle.
    "test"(Test::class) {
        useJUnitPlatform()
    }
}