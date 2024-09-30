FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/sms-service-0.0.1-SNAPSHOT.jar /app/sms-service/target/
WORKDIR /app/sms-service
RUN apt-get update && apt-get install -y default-mysql-client
RUN ls -l /app
CMD ["java", "-jar", "target/sms-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
