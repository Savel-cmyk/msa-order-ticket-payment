FROM gradle:8.11.1-jdk17 AS build

WORKDIR /app/payment-service

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre AS runtime

WORKDIR /app/payment-service

COPY --from=build /app/payment-service/build/libs/*.jar payment-service.jar

EXPOSE $PAYMENT_SERVER_PORT

ENTRYPOINT ["java", "-jar", "payment-service.jar"]