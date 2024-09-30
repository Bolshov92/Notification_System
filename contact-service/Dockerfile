FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/contact-service-0.0.1-SNAPSHOT.jar /app/contact-service.jar
RUN apt-get update && apt-get install -y default-mysql-client
ENTRYPOINT ["java", "-jar", "contact-service.jar"]

EXPOSE 8080
