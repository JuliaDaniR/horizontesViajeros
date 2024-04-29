
# Utiliza la imagen base de OpenJDK 17 con Alpine Linux
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo
WORKDIR /agencia-de-turismo

# Copia el archivo POM y el archivo JAR generado por tu aplicación Spring Boot
COPY pom.xml .
COPY target/agencia-de-turismo-0.0.1-SNAPSHOT.jar app.jar

# Define el comando de entrada para ejecutar la aplicación al iniciar el contenedor
CMD ["java", "-jar", "app.jar"]
