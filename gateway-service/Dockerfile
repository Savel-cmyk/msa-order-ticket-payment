FROM gradle:8.11.1-jdk17 AS build

WORKDIR /app/gateway-service

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app/gateway-service

COPY --from=build /app/gateway-service/build/libs/*.jar gateway-service.jar

EXPOSE $GATEWAY_SERVER_PORT

ENTRYPOINT ["java", "-jar", "gateway-service.jar"]