FROM openjdk:17-jdk-slim

WORKDIR /app
RUN apt-get update && apt-get install -y default-mysql-client
COPY target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service.jar
ENTRYPOINT ["java","-Dserver.port=$PORT", "-jar", "/app/notification-service.jar"]
EXPOSE 8080
