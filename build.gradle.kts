// `build.gradle.kts`
plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("io.quarkus") version "3.20.1"
}

val quarkusPlatformVersion = "3.20.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // BOM dichiarato con platform
    implementation(platform("io.quarkus.platform:quarkus-bom:$quarkusPlatformVersion"))

    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-hibernate-orm-panache-kotlin")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-smallrye-jwt")
    implementation("io.quarkus:quarkus-keycloak-authorization")
    implementation("io.quarkus:quarkus-config-yaml")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "com.tryply"
version = "1.0.0"
description = "TryPly Backend (LTS)"

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

// `settings.gradle.kts`
