FROM gradle:8.11.1-jdk17 AS build

WORKDIR /app/account-service

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app/account-service

COPY --from=build /app/account-service/build/libs/*.jar account-service.jar

EXPOSE $ACCOUNT_SERVER_PORT

ENTRYPOINT ["java", "-jar", "account-service.jar"]