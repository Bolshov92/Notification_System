FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/user-service-0.0.1-SNAPSHOT.jar /app/user-service/target/
WORKDIR /app/user-service
RUN apt-get update && apt-get install -y default-mysql-client
RUN ls -l /app
CMD ["java", "-jar", "target/user-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
