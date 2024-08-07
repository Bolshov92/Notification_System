FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/contact-service.jar /app/contact-service.jar
ENTRYPOINT ["java","-jar","/app/contact-service.jar"]