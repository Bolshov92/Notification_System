#!/bin/bash
set -e
mvn clean package
cp target/*.jar /app/user-service.jar
