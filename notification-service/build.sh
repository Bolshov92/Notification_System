#!/bin/bash
set -e
mvn clean package
cp target/*.jar /app/notification-service.jar
