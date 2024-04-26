
# Utiliza la imagen base de OpenJDK 17
FROM openjdk:17-jdk-alpine

# Define las variables de entorno para la base de datos
ENV DATABASE_URL=jdbc:mysql://java_db:3306/agencia_turismo?useSSL=false&serverTimezone=UTC
ENV DATABASE_USERNAME=root
ENV DATABASE_PASSWORD=root

# Copia el archivo JAR generado por tu aplicación Spring Boot
COPY target/agencia-de-turismo-0.0.1-SNAPSHOT.jar java-app.jar

# Define el comando de entrada para ejecutar la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "java-app.jar"]
