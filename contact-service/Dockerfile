FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/contact-service-0.0.1-SNAPSHOT.jar /app/contact-service/target/
WORKDIR /app/contact-service
RUN apt-get update && apt-get install -y default-mysql-client
RUN ls -l /app
CMD ["java", "-jar", "target/contact-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
