FROM gradle:8.11.1-jdk17 AS build

WORKDIR /app/ticket-service

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app/ticket-service

COPY --from=build /app/ticket-service/build/libs/*.jar ticket-service.jar

EXPOSE $TICKET_SERVER_PORT

ENTRYPOINT ["java", "-jar", "ticket-service.jar"]