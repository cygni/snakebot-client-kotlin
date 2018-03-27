
group = "se.cygni.snake"
version = "1.0-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "1.2.31"
}


application {
    mainClassName = "se.cygni.snake.MainKt"
}

dependencies {
    compile(kotlin("stdlib"))
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5")
    compile("com.squareup.okhttp3:okhttp:3.10.0")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
    compile("ch.qos.logback:logback-classic:1.2.3")
    testCompile("org.assertj:assertj-core:3.8.0")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.0.2")
    testRuntime("org.junit.platform:junit-platform-launcher:1.0.2")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.0.2")
}

repositories {
    jcenter()
}