val ktorVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation(kotlin("stdlib"))
    implementation(project(":core"))
}