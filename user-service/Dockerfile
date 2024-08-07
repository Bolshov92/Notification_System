FROM openjdk:17
WORKDIR /app
COPY target/user.service.jar /app/user-service.jar
ENTRYPOINT ["java", "-jar", "app/user-service.jar"]