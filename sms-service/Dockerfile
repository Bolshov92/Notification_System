FROM openjdk:17-jdk-slim

WORKDIR /app
RUN apt-get update && apt-get install -y default-mysql-client
COPY target/sms-service-0.0.1-SNAPSHOT.jar /app/sms-service.jar
ENTRYPOINT ["java","-Dserver.port=$PORT", "-jar", "/app/sms-service.jar"]
EXPOSE 8080
