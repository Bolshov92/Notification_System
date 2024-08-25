FROM openjdk:17
WORKDIR /app
COPY target/contact-service-0.0.1-SNAPSHOT.jar /app/contact-service.jar
ENTRYPOINT ["java", "-jar", "/app/contact-service.jar"]
EXPOSE 8080