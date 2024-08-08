FROM openjdk:17
WORKDIR /app
COPY target/api-gateway.jar /app/api-gateway.jar
ENTRYPOINT ["java","-jar","/app/api-gateway.jar"]