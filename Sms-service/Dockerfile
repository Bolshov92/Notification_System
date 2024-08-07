FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/sms-service.jar /app/sms-service.jar
ENTRYPOINT ["java","-jar","/app/sms-service.jar"]