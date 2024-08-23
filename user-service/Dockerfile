FROM openjdk:17
WORKDIR /app
COPY target/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]
EXPOSE 8080