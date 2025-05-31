# Etap 1: budowanie aplikacji (Maven 3.9.9 + JDK 21)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etap 2: uruchamianie aplikacji (JDK 21 tylko do runtime)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
