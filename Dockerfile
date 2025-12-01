# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copier uniquement pom.xml pour utiliser le cache
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copier le reste du code
COPY src ./src

# Build
RUN mvn -q clean package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copier le jar du stage build
COPY --from=build /app/target/*.jar app.jar

# Exposer ton port Spring Boot (optionnel pour Render)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
