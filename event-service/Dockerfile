FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/event-service-0.0.1-SNAPSHOT.jar /app/event-service.jar

CMD ["java", "-jar", "/app/event-service.jar"]

EXPOSE 8080
