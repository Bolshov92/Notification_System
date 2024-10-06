FROM openjdk:17-jdk-slim

WORKDIR /app
RUN apt-get update && apt-get install -y default-mysql-client
COPY target/contact-service-0.0.1-SNAPSHOT.jar /app/contact-service.jar
CMD ["java", "-jar", "/app/contact-service.jar"]
EXPOSE 8080
