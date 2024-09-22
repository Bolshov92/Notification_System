FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar /app/user-service.jar
RUN apt-get update && apt-get install -y default-mysql-client
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]
EXPOSE 8080