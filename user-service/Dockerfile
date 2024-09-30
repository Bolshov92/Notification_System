FROM openjdk:17-jdk-slim

WORKDIR /app
RUN apt-get update && apt-get install -y default-mysql-client
COPY target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]
EXPOSE 8080
