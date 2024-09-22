FROM openjdk:17-jdk-slim
WORKDIR /app
RUN apt-get update && apt-get install -y default-mysql-client
COPY target/*.jar /app/contact-service.jar
ENTRYPOINT ["java", "-jar", "/app/contact-service.jar"]
EXPOSE 8080