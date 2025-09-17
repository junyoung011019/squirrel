## 빌드 단계
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# 프로젝트 전체 파일을 컨테이너로 복사
COPY . .

# gradlew 파일에 실행 권한 부여
RUN chmod +x ./gradlew

# Gradle 빌드 실행
RUN ./gradlew clean build --no-daemon -x test

## 런타임 단계
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
LABEL maintainer="junyoung"

COPY --from=builder /app/word-service/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]