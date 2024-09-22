FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar /app/file-service.jar
RUN apt-get update && apt-get install -y default-mysql-client
ENTRYPOINT ["java", "-jar", "/app/file-service.jar"]
EXPOSE 8080