FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service/target/
WORKDIR /app/notification-service
RUN apt-get update && apt-get install -y default-mysql-client
RUN ls -l /app
CMD ["java", "-jar", "target/notification-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
