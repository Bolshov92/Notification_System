FROM openjdk:17
WORKDIR /app
COPY  target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service.jar
ENTRYPOINT ["java", "-jar", "notification-service.jar"]
EXPOSE 8080