FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /app

# Installer Maven
RUN apk add --no-cache maven

COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]