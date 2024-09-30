FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/event-service-0.0.1-SNAPSHOT.jar /app/event-service/target/
WORKDIR /app/event-service
RUN apt-get update && apt-get install -y default-mysql-client
RUN ls -l /app
CMD ["java", "-jar", "target/event-service-0.0.1-SNAPSHOT.jar"]


EXPOSE 8080
