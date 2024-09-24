FROM maven:3.9.7 AS build
WORKDIR /app
COPY pom.xml /app
COPY src /app/src
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/notification-service-0.0.1-SNAPSHOT.jar notification-service.jar
RUN apt-get update && apt-get install -y default-mysql-client
ENTRYPOINT ["java", "-jar", "notification-service.jar"]
EXPOSE 8080
