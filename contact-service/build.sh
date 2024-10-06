#!/bin/bash
set -e
mvn clean package
cp target/*.jar /app/contact-service.jar
