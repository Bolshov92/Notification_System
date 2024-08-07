FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/config-service.jar /app/config-service.jar
ENTRYPOINT ["java", "-jar", "app/config-service.jar"]