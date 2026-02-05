// `build.gradle.kts`
plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("io.quarkus") version "3.20.1"
    id("org.jetbrains.kotlin.kapt") version "2.0.21"
}

val quarkusPlatformVersion = "3.20.1"
val mapstructVersion = "1.6.2"

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
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:$quarkusPlatformVersion"))


    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-hibernate-validator")

    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-hibernate-orm-panache-kotlin")

    implementation("io.quarkus:quarkus-flyway")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    implementation("io.quarkus:quarkus-smallrye-jwt")
    implementation("io.quarkus:quarkus-keycloak-authorization")
    implementation("io.quarkus:quarkus-config-yaml")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
}

group = "com.planit"
version = "1.0.0"
description = "Plan-It Backend"

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}


kapt {
    correctErrorTypes = true
}