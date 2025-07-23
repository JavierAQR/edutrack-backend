# Usa una imagen base con Java y Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el proyecto completo al contenedor
COPY . .

# Compila el proyecto y empaqueta el JAR
RUN mvn clean package -DskipTests

# Fase de ejecución: usa una imagen solo con JDK para correr la app
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia el archivo .jar construido anteriormente
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto por defecto (ajústalo si tu Spring Boot usa otro)
EXPOSE 8080

# Comando para arrancar la app
ENTRYPOINT ["java", "-jar", "app.jar"]