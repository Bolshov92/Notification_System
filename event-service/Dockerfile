FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/event-service-0.0.1-SNAPSHOT.jar /app/event-service.jar
RUN apt-get update && apt-get install -y default-mysql-client
RUN ls -l /app
ENTRYPOINT ["java", "-jar", "event-service.jar"]

EXPOSE 8080
