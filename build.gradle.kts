plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "team.klover"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("io.netty:netty-all:4.1.100.Final")

	// jjwt
	implementation("io.jsonwebtoken:jjwt-api:0.12.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.5")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.google.api-client:google-api-client:2.2.0")

	//swagger
	implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	// redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// aws s3
	implementation(platform("software.amazon.awssdk:bom:2.24.0"))
	implementation("software.amazon.awssdk:s3")

	// elastic search
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")

	//언어 감지
	implementation("com.github.pemistahl:lingua:1.2.2")

	// https://mvnrepository.com/artifact/net.datafaker/datafaker repost 가데이터 밀어넣기
	implementation("net.datafaker:datafaker:2.4.2")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
