FROM gradle:8.11.1-jdk17 AS build

WORKDIR /app/eureka-server

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app/eureka-server

COPY --from=build /app/eureka-server/build/libs/*.jar eureka-server.jar

EXPOSE $EUREKA_SERVER_PORT

ENTRYPOINT ["java", "-jar", "eureka-server.jar"]