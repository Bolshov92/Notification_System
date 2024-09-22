FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service.jar
RUN apt-get update && apt-get install -y default-mysql-client
ENTRYPOINT ["java", "-jar", "/app/notification-service.jar"]
EXPOSE 8080