FROM gradle:8.11.1-jdk17 AS build

WORKDIR /app/order-service

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app/order-service

COPY --from=build /app/order-service/build/libs/*.jar order-service.jar

EXPOSE $ORDER_SERVER_PORT

ENTRYPOINT ["java", "-jar", "order-service.jar"]