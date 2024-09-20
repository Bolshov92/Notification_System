FROM openjdk:17
WORKDIR /app
COPY target/sms-service-0.0.1-SNAPSHOT.jar /app/sms-service.jar
ENTRYPOINT ["java", "-jar", "/app/sms-service.jar"]
EXPOSE 8080
