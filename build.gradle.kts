import org.gradle.kotlin.dsl.java

plugins {

    java
    id("org.springframework.boot") version "3.4.1" apply false
   id("io.spring.dependency-management") version "1.1.7" apply false
}

allprojects {
    group = "com.aetherstream"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    // This block automatically applies to every folder listed in settings.gradle.kts
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(23))
        }
    }

    dependencies {
        // Every single module will have access to these
        compileOnly("org.projectlombok:lombok:1.18.34")
        annotationProcessor("org.projectlombok:lombok:1.18.34")

        // Essential for Reactive programming
        implementation("io.projectreactor:reactor-core")
        testImplementation("io.projectreactor:reactor-test")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        implementation(platform("org.springframework.boot:spring-boot-dependencies:4.0.5"))
        implementation("org.springframework.boot:spring-boot-starter")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}