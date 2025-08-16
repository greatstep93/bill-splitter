plugins {
    java
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.greatstep"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
    implementation("org.apache.commons:commons-lang3:3.18.0")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.10.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.19.2")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.security:spring-security-test")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    named<JavaExec>("bootRun") {
        jvmArgs("--add-opens=java.base/sun.reflect.annotation=ALL-UNNAMED")
    }

    named<Test>("test") {
        jvmArgs("--add-opens=java.base/sun.reflect.annotation=ALL-UNNAMED")
    }
}
