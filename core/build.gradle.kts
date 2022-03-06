val logbackVersion: String by project
val kotlinxCoroutinesVersion: String by project
val kotlinxReactorVersion: String by project

plugins {
    kotlin("jvm")
}

configurations {
    create("test")
}

tasks.register<Jar>("testArchive") {
    archiveBaseName.set("core-test")
    from(project.the<SourceSetContainer>()["test"].output)
}

artifacts {
    add("test", tasks["testArchive"])
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$kotlinxCoroutinesVersion")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:$kotlinxReactorVersion")
    implementation(kotlin("stdlib"))
    testImplementation("ch.qos.logback:logback-classic:$logbackVersion")
}