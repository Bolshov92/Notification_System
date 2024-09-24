FROM maven:3.9.7 AS build
WORKDIR /app
COPY pom.xml /app
COPY src /app/src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/event-service-0.0.1-SNAPSHOT.jar event-service.jar
ENTRYPOINT ["java", "-jar", "event-service.jar"]
EXPOSE 8080
