# Etapa de construcción
FROM maven:3.9-eclipse-temurin-23 as build

COPY . /app
WORKDIR /app

RUN mvn clean package -DskipTests

# Etapa de empaquetado
FROM eclipse-temurin:23

ARG JAR_FILE=target/*.jar
COPY --from=build /app/target/*.jar app.jar

EXPOSE ${PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]
