
# Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
# Click nbfs://nbhost/SystemFileSystem/Templates/Other/Dockerfile to edit this template

FROM openjdk:17-jdk-alpine

env DATABASE_URL jdbc:mysql://localhost:3306/agencia_turismo?useSSL=false&serverTimezone=UTC
env DATABASE_USERNAME root
env DATABASE_PASSWORD root

copy target/agencia-de-turismo-0.0.1-SNAPSHOT.jar java-app.jar

entrypoint ["java" , "-jar" , "java-app.jar"]
