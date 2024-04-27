
# Utiliza la imagen base de OpenJDK 17 con Alpine Linux
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo
WORKDIR /agencia-de-turismo

# Copia el archivo POM y el archivo JAR generado por tu aplicación Spring Boot
COPY pom.xml .
COPY target/agencia-de-turismo-0.0.1-SNAPSHOT.jar app.jar

# Instala Maven en el contenedor
RUN apk add --no-cache maven

# Ejecuta el comando "mvn clean install" para construir la aplicación
RUN mvn clean install

# Define el comando de entrada para ejecutar la aplicación al iniciar el contenedor
CMD ["java", "-jar", "app.jar"]

ENV MYSQLDB_USER=root
ENV MYSQLDB_ROOT_PASSWORD=root
ENV MYSQLDB_DATABASE=agencia-de-turismo
ENV MYSQLDB_LOCAL_PORT=3307
ENV MYSQLDB_DOCKER_PORT=3306

ENV SPRING_LOCAL_PORT=6868
ENV SPRING_DOCKER_PORT=8080