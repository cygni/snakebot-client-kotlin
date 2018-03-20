
group = "se.cygni.snakebot"
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
}

repositories {
    jcenter()
}