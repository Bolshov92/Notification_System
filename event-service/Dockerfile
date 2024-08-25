FROM openjdk:17
WORKDIR /app
COPY target/event-service-0.0.1-SNAPSHOT.jar /app/event-service.jar
ENTRYPOINT ["java","-jar","/app/event-service.jar"]
EXPOSE 8080