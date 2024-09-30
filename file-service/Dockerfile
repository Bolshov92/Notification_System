FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/file-service-0.0.1-SNAPSHOT.jar /app/file-service/target/
WORKDIR /app/file-service
RUN apt-get update && apt-get install -y default-mysql-client
RUN ls -l /app
CMD ["java", "-jar", "target/file-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
