FROM openjdk:17-alpine
WORKDIR /app
COPY target/event-service.jar/ app/event-service.jar
ENTRYPOINT ["java","-jar","/app/event-service.jar"]