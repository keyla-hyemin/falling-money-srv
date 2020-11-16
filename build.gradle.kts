plugins {
    val springBootPluginVersion = "2.3.5.RELEASE"
    val lombokPluginVersion = "5.1.0"

    id ("org.springframework.boot") version springBootPluginVersion
    id ("io.spring.dependency-management") version "1.0.10.RELEASE"

    id("io.freefair.lombok") version lombokPluginVersion

    java
}

group = "com.kkp"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("org.springframework.boot:spring-boot-starter-data-redis")
    implementation ("org.springframework:spring-jdbc")

    implementation("org.apache.commons:commons-lang3:3.11")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit", module = "junit")
        exclude(group="org.junit.vintage", module="junit-vintage-engine")
    }
    testImplementation ("io.projectreactor:reactor-test")
}

//test {
//    useJUnitPlatform()
//}
