FROM maven:3.9.7 AS build
WORKDIR /app
COPY pom.xml /app
COPY src /app/src
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/contact-service-0.0.1-SNAPSHOT.jar contact-service.jar
ENTRYPOINT ["java", "-jar", "contact-service.jar"]
EXPOSE 8080
