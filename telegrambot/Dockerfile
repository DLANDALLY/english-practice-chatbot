FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY . .

RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY .env /app/.env

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]
