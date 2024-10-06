#!/bin/bash
set -e
mvn clean package
cp target/*.jar /app/file-service.jar
