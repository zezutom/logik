import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val junitVersion: String by project

plugins {
    kotlin("jvm") version "1.6.10"
}

group = "io.logik"
version = "0.1"

allprojects {
    apply {
        plugin("kotlin")
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    }
    tasks.test {
        useJUnitPlatform()
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
}

