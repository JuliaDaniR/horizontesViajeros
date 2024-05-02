FROM openjdk:17-jdk-slim

COPY target/agencia-de-turismo-0.0.1-SNAPSHOT.jar app_turismo.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app_turismo.jar"]
