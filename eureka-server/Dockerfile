FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/eureka-server.jar /app/eureka-server.jar
ENTRYPOINT ["java","-jar","/app/eureka-server.jar"]