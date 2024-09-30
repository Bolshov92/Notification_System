FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/sms-service-0.0.1-SNAPSHOT.jar /app/sms-service.jar
RUN apt-get update && apt-get install -y default-mysql-client
ENTRYPOINT ["java", "-jar", "sms-service.jar"]

EXPOSE 8080
