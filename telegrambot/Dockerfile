FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY . .

RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app
RUN apt-get update && apt-get install -y ffmpeg

COPY --from=build /app/target/*.jar app.jar
COPY .env /app/.env

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "app.jar"]
