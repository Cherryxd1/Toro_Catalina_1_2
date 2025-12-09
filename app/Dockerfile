# Stage 1: Build con Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos de configuración primero (para cache de dependencias)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime ligero
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR compilado
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]