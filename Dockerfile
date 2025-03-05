# 베이스 이미지 설정 (OpenJDK 사용)
FROM openjdk:17-jdk-slim

# Java 버전 확인 (확인 후 적절한 버전 설치)
RUN java -version

# 작업 디렉터리 설정
WORKDIR /app

# JAR 파일 복사
COPY build/libs/server-0.0.1-SNAPSHOT.jar app.jar

# 환경 변수 설정 (Spring 프로파일)
ENV SPRING_PROFILES_ACTIVE=prod

# 실행 명령어
CMD ["java", "-jar", "app.jar"]
