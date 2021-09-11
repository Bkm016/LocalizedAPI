plugins {
    java
    id("io.izzel.taboolib") version "1.26"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    install("common")
    install("module-nms", "module-nms-util")
    install("platform-bukkit")
    classifier = null
    version = "6.0.1-8"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("ink.ptms.core:v11600:11600:all")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
