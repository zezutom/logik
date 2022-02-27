val logbackVersion: String by project
val kotlinxCoroutinesVersion: String by project
val kotlinxReactorVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$kotlinxCoroutinesVersion")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:$kotlinxReactorVersion")
    implementation(kotlin("stdlib"))
    testImplementation("ch.qos.logback:logback-classic:$logbackVersion")
}