FROM openjdk:17-alpine
WORKDIR /app
COPY target/user.service.jar /app/user-service.jar
ENTRYPOINT ["java", "-jar", "app/user-service.jar"]