#!/bin/bash
set -e
mvn clean package
cp target/*.jar /app/event-service.jar
