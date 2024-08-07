FROM openjdk:17-alpine
WORKDIR /app
COPY  target/file-service.jar /app/file-service.jar
ENTRYPOINT ["java", "-jar" ,""/app/file-service.jar"]