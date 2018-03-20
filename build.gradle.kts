
group = "se.cygni.snake"
version = "1.0-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "1.2.30"
}

application {
    mainClassName = "se.cygni.snake.HelloWorldKt"
}

dependencies {
    compile(kotlin("stdlib"))
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5")
    compile("com.squareup.okhttp3:okhttp:3.10.0")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
}

repositories {
    jcenter()
}